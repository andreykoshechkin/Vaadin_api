package org.example.vaadin_api.view;

import ch.qos.logback.core.joran.event.BodyEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.*;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import org.example.vaadin_api.data.MessageTemplate;
import org.example.vaadin_api.service.MessageTemplateService.MessageTemplateService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Route("/api")
@UIScope
@Component
public class CCRequest extends VerticalLayout {

    private Binder<MessageTemplate> messageTemplateBinder;
    private ComboBox<String> messageTemplateComboBox;
    private ComboBox<String> phoneNumberComboBox;
    private ComboBox<LocalDate> timeCombobox;
    private Button saveButton;
    private MessageTemplateValidate messageTemplateValidate;
    private MessageTemplateService messageTemplateService;


    @PostConstruct
    public void init() {
        saveButton = new Button("Save");
        messageTemplateValidate = new MessageTemplateValidate(messageTemplateComboBox);
        // Инициализация компонентов
        messageTemplateComboBox = new ComboBox<>("Шаблон сообщения");
        phoneNumberComboBox = new ComboBox<>("Номер телефона");
        timeCombobox = new ComboBox<>("Время");
        messageTemplateService = new MessageTemplateService();

        // Создаем Binder для MessageTemplate
        messageTemplateBinder = new Binder<>(MessageTemplate.class);

        // Установка данных в ComboBox
        phoneNumberComboBox.setItems("+79964717230", "+799988811122");
        messageTemplateComboBox.setItems(messageTemplateService.getMessageTemplate());
        timeCombobox.setItems(LocalDate.now());


        // Валидация для номера телефона
        messageTemplateBinder.forField(timeCombobox)
                .withValidator((Validator<LocalDate>) (localDate, valueContext) -> {
                    if (localDate == null) {
                        return ValidationResult.error("Укажите дату");
                    }
                    return ValidationResult.ok();
                })
                .bind(MessageTemplate::getLocalDate, MessageTemplate::setLocalDate);

        messageTemplateBinder.forField(phoneNumberComboBox)
                .bind(MessageTemplate::getMessageTemplate, MessageTemplate::setMessageTemplate);


        phoneTemplateValueChangeListener();

        add(phoneNumberComboBox, messageTemplateComboBox, timeCombobox, saveButton);
    }

    private void phoneTemplateValueChangeListener() {
        messageTemplateComboBox.addValueChangeListener(event ->
                validatePhoneByTemplateData(event.getValue(), phoneNumberComboBox.getValue()));
        phoneNumberComboBox.addValueChangeListener(event ->
                validatePhoneByTemplateData(messageTemplateComboBox.getValue(), event.getValue()));
    }


    private void validatePhoneByTemplateData(String template, String phoneNumber){
        boolean isValidationRequired = templatesRequiringValidation().contains(template);
        if (isValidationRequired && (phoneNumber == null || phoneNumber.trim().isEmpty())) {
            phoneNumberComboBox.setErrorMessage("Ошибка валидации: номер телефона обязателен");
            phoneNumberComboBox.setInvalid(true);
            saveButton.setEnabled(false);
            return;
        }
        phoneNumberComboBox.setInvalid(false);
        saveButton.setEnabled(true);
    }

    private Set<String> templatesRequiringValidation() {
        return Set.of("Временный пароль", "Повторное обращение");
    }
}