package com.msmeli.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ExceptionDTO {
    private String mensaje;
    private String reason;
    private int codigohttp;
    private String zone;
    private String codigoError;
}
