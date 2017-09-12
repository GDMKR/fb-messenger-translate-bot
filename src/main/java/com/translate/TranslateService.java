package com.translate;

import com.translate.multillect.MultillectAnswer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;



@Service
public class TranslateService {

    private static final Logger logger = LoggerFactory.getLogger(TranslateService.class);

    private static String secretKey;
    private static String accountID;


    protected static final String ENCODING = "UTF-8";

    private static final String SERVICE_URL = "https://api.multillect.com/translate/json/1.0/";

    public String translate(String sourceText, Languages sourceLanguage, Languages targetLanguage) throws Exception {
        validateService(sourceText);

        String methodParameter = "?method=translate/api/translate";
        String fromParameter = "&from=";
        String toParameter = "&to=";
        String textParameter = "&text=";
        String keyParameter = "&sig=";

  String url = SERVICE_URL + accountID
        + methodParameter
        + fromParameter + sourceLanguage.toString()
        + toParameter + targetLanguage.toString()
        + textParameter + sourceText.toString()
        + keyParameter + secretKey.toString();

        RestTemplate restTemplate = new RestTemplate();

        MultillectAnswer multillectAnswer = restTemplate.getForObject(url, MultillectAnswer.class);
        logger.info(multillectAnswer.toString());

        String targetText = "";
        try{
            targetText = multillectAnswer.getResult().getTranslated();
            logger.info("Text: '{}' was translated from '{}' to '{}' ", sourceText, sourceLanguage.toString(), targetLanguage.toString());
        }
        catch(NullPointerException e){
            logger.error(multillectAnswer.getError().toString());
            targetText = "Error";
        }

        return targetText;
    }


    private static void validateService(final String text) throws Exception {
        final int byteLength = text.getBytes(ENCODING).length;
        if(byteLength>10240) { // TODO What is the maximum text length allowable for Yandex?
            throw new RuntimeException("TEXT_TOO_LARGE");
        }
        if(secretKey==null || secretKey.length() > 32) {
            throw new RuntimeException("INVALID_SECRET_KEY - Please set the Secret Key.");
        }
        if(accountID==null) {
            throw new RuntimeException("INVALID_ACCOUNT_ID - Please set the Account ID.");
        }
    }


    public static void setSecretKey(String secretKey) {
        TranslateService.secretKey = secretKey;
    }

    public static void setAccountID(String accountID) {
        TranslateService.accountID = accountID;
    }
}
