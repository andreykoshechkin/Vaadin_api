package org.example.vaadin_api.service.MessageTemplateService;



import org.example.vaadin_api.data.MessageTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageTemplateService {

    public List<String> getMessageTemplate(){
        return List.of("Повторное обращение", "Временный пароль", "Заявка на подключение");
    }
}
