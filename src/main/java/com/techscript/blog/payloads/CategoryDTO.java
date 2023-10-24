package com.techscript.blog.payloads;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@NoArgsConstructor
@Getter
@Setter
public class CategoryDTO {

    private Integer categoryId;
    @NotEmpty
    @Size(min = 3, max = 25, message = "Between 3 and 25 characters ")
    private String  categoryTitle;


    @NotEmpty
    private String  categoryDescription;}
