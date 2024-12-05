package com.truecaller.telegram;


import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.truecaller.entities.Profile;
import com.truecaller.projections.CallerID;
import com.truecaller.services.OtpService;
import com.truecaller.services.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramWebhookBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class WebhookBot extends TelegramWebhookBot {


    private final ProfileService profileService;

    private final OtpService otpService;
    private String botUsername;
    private String botToken;
    private String botPath;
    private Logger logger = LoggerFactory.getLogger(WebhookBot.class);
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

//    public WebhookBot(String BOT_TOKEN){
//        super(BOT_TOKEN);
//
//    }
    public WebhookBot(String botToken, ProfileService profileService, OtpService otpService) {
        super(botToken);
        this.profileService = profileService;
        this.otpService = otpService;
    }
    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        Message msg = update.getMessage();
        User user = msg.getFrom();
        Long id = user.getId();
        BotApiMethod<?> replyMessageToUser = null;
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
            if (requestersProfileOptional.isPresent()) {
                Profile requestersProfile = requestersProfileOptional.get();
                String otp = otpService.sendToTelegram(mobileNumber);
                confirmationMessage = String.format("""
                    Thanks! We received your phone number as \s
                    country code : %s \s
                    mobile number : %s \s
                    Your otp is : %s \s""",callerID.getCountryCode(),callerID.getNumber(),otp);
            } else {
                confirmationMessage = String.format("""
                    Thanks! We received your phone number as \s
                    country code : %s \s
                    mobile number : %s \s
                    You are not registerd truecaller user please register""",callerID.getCountryCode(),callerID.getNumber());
            }
            replyMessageToUser = sendText(id, confirmationMessage);
        } else if(msg.isCommand()){
            if(msg.getText().equals("/requestotp")){
                replyMessageToUser = requestPhoneNumber(id);
            } else if (msg.getText().equals("/review")){
                replyMessageToUser = sendText(id,"Whats your review ? ");
            }
        } else {
            replyMessageToUser = sendText(id,"Invalid request / message");
        }
        return replyMessageToUser;
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
    public SendMessage requestPhoneNumber(Long chatId) {
        String answer = "Please share your phone number to receive the OTP:";

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
