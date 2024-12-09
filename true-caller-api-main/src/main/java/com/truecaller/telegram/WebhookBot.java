package com.truecaller.telegram;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.truecaller.entities.Profile;
import com.truecaller.entities.ReviewBotState;
import com.truecaller.projections.CallerID;
import com.truecaller.projections.Review;
import com.truecaller.services.OtpService;
import com.truecaller.services.ProfileService;
import com.truecaller.services.ReviewBotStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WebhookBot extends TelegramWebhookBot {


    private final ProfileService profileService;

    private final OtpService otpService;
    private final ReviewBotStateService reviewBotStateService;
    private String botUsername;
    private String botToken;
    private String botPath;
    private Logger logger = LoggerFactory.getLogger(WebhookBot.class);

    // RestTemplate to interact with Spring Boot API
    private RestTemplate restTemplate = new RestTemplate();
    private String companyBotApiUrl = "http://localhost:8081/api/companyBot/companyBotMenu";

    @Override
    public String getBotPath() {
        return botPath;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    public void setBotUsername(String botUsername) {
        this.botUsername = botUsername;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public void setBotPath(String botPath) {
        this.botPath = botPath;
    }


    public WebhookBot(String botToken, ReviewBotStateService reviewBotStateService,
                      ProfileService profileService, OtpService otpService) {
        super(botToken);
        this.profileService = profileService;
        this.otpService = otpService;
        this.reviewBotStateService = reviewBotStateService;
    }
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Message msg = update.getMessage();
        User user = msg.getFrom();
        Long id = user.getId();
        BotApiMethod<?> replyMessageToUser = null;
        ReviewBotState reviewBotState = reviewBotStateService.getBotState(this.getBotUsername(),id);
        BotState state = reviewBotState.getState();
        Review usersReview = reviewBotState.getReview();
        if (msg.hasContact()) {
            String mobileNumber = msg.getContact().getPhoneNumber();
            logger.info("mobilenumber before: "+mobileNumber);
            if (!mobileNumber.startsWith("+")) {
                mobileNumber = "+" + mobileNumber;
            }
            logger.info("mobilenumber after: "+mobileNumber);
            CallerID callerID = extractCallerID(mobileNumber);
            Optional<Profile> requestersProfileOptional = profileService.getProfileByCallerID(callerID.getNumber(),callerID.getCountryCode());
            String confirmationMessage = "";
            if (requestersProfileOptional.isPresent() && "mouthshut".equals(this.getBotUsername())) {
                Profile requestersProfile = requestersProfileOptional.get();
                String otp = otpService.sendToTelegram(mobileNumber);
                confirmationMessage = String.format("""
                    Thanks! We received your phone number as \s
                    country code : %s \s
                    mobile number : %s \s
                    Your otp is : %s \s""",callerID.getCountryCode(),callerID.getNumber(),otp);
            } else if ("mouthshut".equals(this.getBotUsername())){
                confirmationMessage = String.format("""
                    Thanks! We received your phone number as \s
                    country code : %s \s
                    mobile number : %s \s
                    You are not registerd mouthshut user please register""",callerID.getCountryCode(),callerID.getNumber());
            } else if (requestersProfileOptional.isPresent() && BotState.AWAITING_PHONE_NUMBER==state){
                Profile reviewersProfile = requestersProfileOptional.get();
                if(reviewersProfile.isVerified()){
                    usersReview.setReviewer(new CallerID(reviewersProfile.getPhoneNumber(),reviewersProfile.getCountryCode()));
                    state = BotState.AWAITING_REVIEW;
                    reviewBotStateService.saveOrUpdateBotState(this.getBotUsername(),id,state,usersReview);
                    confirmationMessage = String.format("""
                    Thanks! We received your phone number as \s
                    country code : %s \s
                    mobile number : %s \s
                    You are a verified user of mouthshut. Please enter your review \s""",callerID.getCountryCode(),callerID.getNumber());
                } else {
                    confirmationMessage = String.format("""
                    Thanks! We received your phone number as \s
                    country code : %s \s
                    mobile number : %s \s
                    You are not a verified mouthshut user please perform otp verification at https://t.me/MeheryOtpbot""",callerID.getCountryCode(),callerID.getNumber());
                    reviewBotStateService.deleteBotState(this.getBotUsername(),id);
                }
            } else if(requestersProfileOptional.isEmpty() && BotState.AWAITING_PHONE_NUMBER==state){
                confirmationMessage = String.format("""
                    Thanks! We received your phone number as \s
                    country code : %s \s
                    mobile number : %s \s
                    You are not registerd mouthshut user please register""",callerID.getCountryCode(),callerID.getNumber());
                reviewBotStateService.deleteBotState(this.getBotUsername(),id);
            }
            replyMessageToUser = sendText(id, confirmationMessage);
        } else if(msg.isCommand()){
            if(msg.getText().equals("/requestotp")){
                replyMessageToUser = requestPhoneNumber(id,"Please share your phone number to receive the OTP:");
            } else if (msg.getText().equals("/review")){
                reviewBotStateService.saveOrUpdateBotState(this.getBotUsername(),id,BotState.AWAITING_PHONE_NUMBER,new Review(this.getBotUsername()));
                replyMessageToUser = requestPhoneNumber(id,"Please share your contact to verify your contact : ");
            }
            else if(msg.getText().equals("/companymenu")) {
                replyMessageToUser = getCompanyBotMenu(id); // Fetch and display the company bot menu
            }
        } else if(state==BotState.AWAITING_REVIEW){
            String review = msg.getText();
            usersReview.setReview(review);
            replyMessageToUser = sendText(id,"Enter a rating between 1 and 5 :");
            reviewBotStateService.saveOrUpdateBotState(this.getBotUsername(),id,BotState.AWAITING_RATING,usersReview);
        } else if (state==BotState.AWAITING_RATING) {
            String userInput = msg.getText();
            if (userInput.matches("^[1-5](\\.\\d{1,2})?$")) {
                float rating = Float.parseFloat(userInput);
                // Process the rating
                usersReview.setRating(rating);
                registerReview(usersReview);
                reviewBotStateService.deleteBotState(this.getBotUsername(),id);
                replyMessageToUser = sendText(id,"Thanks! You provided a rating of : " + rating+" .Your review has been registered.");
            } else {
                // Handle invalid input
                replyMessageToUser = sendText(id,"Invalid rating. Please enter a number between 1.0 and 5.0.");
            }
        } else {
            replyMessageToUser = sendText(id,"Invalid request / message");
        }
        return replyMessageToUser;
    }
    public void registerReview(Review userReview){
        if (userReview != null) {
            // Use RestTemplate to send the review to the endpoint
            try {
                RestTemplate restTemplate = new RestTemplate();
                String endpointUrl = "http://localhost:8081/api/reviews/registerReview";

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);

                HttpEntity<Review> request = new HttpEntity<>(userReview, headers);
                ResponseEntity<Review> response = restTemplate.postForEntity(endpointUrl, request, Review.class);

                if (response.getStatusCode() == HttpStatus.OK) {
                    // Review successfully saved
                    logger.info("Thanks! Your review has been recorded.");

                } else {
                    logger.info("Sorry, there was an error saving your review. Please try again.");

                }
            } catch (Exception e) {
                logger.error("Error sending review to endpoint", e);
                logger.info("An error occurred while saving your review. Please try again later.");
            }
        } else {
            logger.info("Something went wrong. Please start again by sending /review.");

        }
    }
    // Fetch the company bot menu from Spring Boot API
    public SendMessage getCompanyBotMenu(Long chatId) {
        try {
            // Fetch the company bot menu from the Spring Boot backend
            List<String> companyMenu = restTemplate.getForObject(companyBotApiUrl, List.class);

            // Prepare the message content
            String menuMessage = "Company Bot Menu: \n" + String.join("\n", companyMenu);
            // Send the message to the user
            return sendText(chatId, menuMessage);
        } catch (HttpClientErrorException e) {
            logger.error("Error fetching company bot menu: {}", e.getMessage());
            return sendText(chatId, "Sorry, we couldn't fetch the company bot menu right now.");

        }
    }
    public CallerID extractCallerID(String numberStr){
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        CallerID callerID = new CallerID();
        try {
            Phonenumber.PhoneNumber numberProto = phoneUtil.parse(numberStr, "");
            callerID.setCountryCode("+"+Integer.toString(numberProto.getCountryCode()));
            callerID.setNumber(Long.toString(numberProto.getNationalNumber()));
            logger.info("Country code: " + callerID.getCountryCode());
            logger.info("National number: " + callerID.getNumber());
        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }
        return callerID;
    }
    public SendMessage sendText(Long who, String what){
        SendMessage sm = SendMessage.builder()
                .chatId(who.toString()) //Who are we sending a message to
                .text(what).build();    //Message content
        return sm;
    }
    public SendMessage requestPhoneNumber(Long chatId,String answer) {

        // Create a ReplyKeyboardMarkup to ask for contact
        ReplyKeyboardMarkup keyboardMarkup = new ReplyKeyboardMarkup();
        keyboardMarkup.setResizeKeyboard(true); // Optional: adjust size for mobile
        keyboardMarkup.setOneTimeKeyboard(true); // Keyboard will disappear after use

        // Create the button that requests contact information
        KeyboardButton contactButton = new KeyboardButton("Send your phone number");
        contactButton.setRequestContact(true);

        // Add the button to a keyboard row
        KeyboardRow row = new KeyboardRow();
        row.add(contactButton);

        // Add the row to the keyboard
        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(row);
        keyboardMarkup.setKeyboard(keyboard);

        // Send the message with the custom keyboard
        SendMessage requestMessage = new SendMessage();
        requestMessage.setChatId(chatId.toString());
        requestMessage.setText(answer);
        requestMessage.setReplyMarkup(keyboardMarkup);

        return requestMessage;
    }

}
