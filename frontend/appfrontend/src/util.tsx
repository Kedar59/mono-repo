async function getResponseData<T>(response: Response): Promise<T> {
    // Check if the response is successful
    if (!response.ok) {
        // Throw an error with the status code
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    // Parse and return the JSON data
    return await response.json();
}

export default getResponseData;