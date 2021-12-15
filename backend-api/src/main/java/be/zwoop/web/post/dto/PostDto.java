package be.zwoop.web.post.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@Data
public class PostDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String text;

    @NotEmpty
    @DecimalMin("1.0")
    private double bidPrice;

    @NotEmpty
    @Size(min = 1, max = 5)
    private List<Long> tagIds;
}
