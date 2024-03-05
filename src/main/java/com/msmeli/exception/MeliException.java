package com.msmeli.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@NoArgsConstructor
@Getter
@Setter
@Component
public class MeliException extends Exception{
    private String error;
    private String reason;
    private String codigoInterno;
    private int status;
    private String zone;


    public MeliException(String message, String codigoInterno, String error, int status, String zone, List<String> cause) throws JsonProcessingException {
        super(message);
        this.codigoInterno = codigoInterno;
        this.zone = zone;
        this.error = error;
        this.status = status;
    }
    public static String[] extractErrorInfo(String errorString) {
        String[] result = new String[2];
        Pattern pattern = Pattern.compile("(\\[.*?\\].*?\\[.*?\\].*?\\[.*?\\])");
        Matcher matcher = pattern.matcher(errorString);
        if (matcher.find()) {
            result[0] = matcher.group();
        }
        pattern = Pattern.compile("\"message\":\"(.*?)\"");
        matcher = pattern.matcher(errorString);
        if (matcher.find()) {
            result[1] = matcher.group(1);
        }
        return result;
    }
    public static MeliException NewMeliException(String message, String reason, String codigoInterno, int status, String zone, List<String> cause) throws JsonProcessingException {
        String[] result = extractErrorInfo(message) ;
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(reason);
            String cutReason = jsonNode.get("message").asText();
            return new MeliException(result[0],codigoInterno,result[1],status,zone,cause);
        }
    }

