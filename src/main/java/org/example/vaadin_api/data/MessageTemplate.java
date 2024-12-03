package org.example.vaadin_api.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageTemplate {

    private String messageTemplate;
    private LocalDate localDate;


}
