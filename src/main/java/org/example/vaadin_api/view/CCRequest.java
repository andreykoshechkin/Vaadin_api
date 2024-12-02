package org.example.vaadin_api.view;

import ch.qos.logback.core.joran.event.BodyEvent;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.BinderValidationStatus;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.ValueContext;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.UIScope;
import jakarta.annotation.PostConstruct;
import org.example.vaadin_api.data.MessageTemplate;
import org.example.vaadin_api.service.MessageTemplateService.MessageTemplateService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Route("/api")
@UIScope
@Component
public class CCRequest extends VerticalLayout {

    private Binder<MessageTemplate> messageTemplateBinder;
    private ComboBox<String> messageTemplateComboBox;
    private ComboBox<String> phoneNumberComboBox;
    private Button saveButton;

    private MessageTemplateService messageTemplateService;


    @PostConstruct
    public void init() {
        saveButton = new Button("Save");

        // Инициализация компонентов
        messageTemplateComboBox = new ComboBox<>("Шаблон сообщения");
        phoneNumberComboBox = new ComboBox<>("Номер телефона");
        messageTemplateService = new MessageTemplateService();

        // Создаем Binder для MessageTemplate
        messageTemplateBinder = new Binder<>(MessageTemplate.class);

        // Установка данных в ComboBox
        phoneNumberComboBox.setItems("+79964717230", "+799988811122");
        messageTemplateComboBox.setItems(messageTemplateService.getMessageTemplate());


        // Валидация для номера телефона
        messageTemplateBinder.forField(phoneNumberComboBox)
                .withValidator(new MessageTemplateValidate(messageTemplateComboBox))
                .bind(MessageTemplate::getMessageTemplate, MessageTemplate::setMessageTemplate);

        // Обработчик изменения шаблона сообщения
        messageTemplateComboBox.addValueChangeListener(event -> {
            messageTemplateBinder.validate();
            saveButton.setEnabled(messageTemplateBinder.isValid());
        });
        phoneNumberComboBox.addValueChangeListener(event -> {
            // Если значение телефона изменилось, проверяем валидность
            saveButton.setEnabled(messageTemplateBinder.isValid());
        });

        // Добавляем компоненты на страницу
        add(phoneNumberComboBox, messageTemplateComboBox, saveButton);
    }

}