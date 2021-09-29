package com.senior.cyber.tv.web.validator;

import com.senior.cyber.tv.dao.entity.Channel;
import com.senior.cyber.tv.web.repository.ChannelRepository;
import com.senior.cyber.frmk.common.base.WicketFactory;
import org.apache.wicket.validation.IValidatable;
import org.apache.wicket.validation.IValidator;
import org.apache.wicket.validation.ValidationError;
import org.springframework.context.ApplicationContext;

import java.util.Optional;

public class ChannelNameValidator implements IValidator<String> {

    private Long id = null;

    public ChannelNameValidator() {
    }

    public ChannelNameValidator(Long id) {
        this.id = id;
    }

    @Override
    public void validate(IValidatable<String> validatable) {
        String name = validatable.getValue();
        if (name != null && !"".equals(name)) {
            ApplicationContext context = WicketFactory.getApplicationContext();
            ChannelRepository channelRepository = context.getBean(ChannelRepository.class);
            Optional<Channel> optionalChannel = channelRepository.findByName(name);
            Channel channel = optionalChannel.orElse(null);
            if (this.id == null) {
                if (channel != null) {
                    validatable.error(new ValidationError(name + " is taken"));
                }
            } else {
                if (channel != null && !channel.getId().equals(this.id)) {
                    validatable.error(new ValidationError(name + " is taken"));
                }
            }
        }
    }

}
