package com.translate.multillect;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonIgnoreProperties(ignoreUnknown = true)
@Setter
@Getter
@NoArgsConstructor
@ToString
public class MultillectAnswer {

    private Result result;
    private Error error;
    private Integer timestamp;
    private Integer id;

}
