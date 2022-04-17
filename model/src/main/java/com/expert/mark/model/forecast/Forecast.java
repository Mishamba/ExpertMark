package com.expert.mark.model.forecast;

import com.expert.mark.model.forecast.method.MethodData;
import com.expert.mark.model.forecast.method.type.MethodType;
import lombok.*;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Forecast {
    private MethodData methodData;
    private MethodType methodType;
    private String assetName;
    private String ownerUsername;
    private Date createDate;
    private Date targetDate;
}
