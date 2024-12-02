package org.example.vaadin_api.view;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.data.binder.ValidationResult;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.binder.ValueContext;

import java.util.HashMap;
import java.util.Map;


public class MessageTemplateValidate implements Validator<String> {
    private static final String ERROR_MESSAGE = "Не заполено обязательно поле";
    private final ComboBox<String> messageTemplateComboBox;

    public MessageTemplateValidate(ComboBox<String> messageTemplateComboBox) {
        this.messageTemplateComboBox = messageTemplateComboBox;
    }

    private Map<String, Boolean> messageTemplateData() {
        Map<String, Boolean> data = new HashMap<>();
        data.put("Временный пароль", true);
        data.put("Заявка на подключение", true);
        data.put("Повторное обращение", true);
        return data;
    }

    @Override
    public ValidationResult apply(String phoneValue, ValueContext valueContext) {
        String template = messageTemplateComboBox.getValue(); // Получаем текущее значение шаблона
        Boolean isMessageTemplateNeedValidation = messageTemplateData().getOrDefault(template, false);

        if (isMessageTemplateNeedValidation && (phoneValue == null || phoneValue.trim().isEmpty())) {
            return ValidationResult.error(ERROR_MESSAGE);
        }
        return ValidationResult.ok();
    }
}