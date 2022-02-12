package be.zwoop.web.post.dto;

import be.zwoop.amqp.domain.common.TagDto;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class PostDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String text;

    @NotNull
    private BigDecimal bidPrice;

    @NotEmpty
    private String currencyCode;

    @NotEmpty
    @Size(min = 1, max = 3)
    private List<TagDto> tags;
}
