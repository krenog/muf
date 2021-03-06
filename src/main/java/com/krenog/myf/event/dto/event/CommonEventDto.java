package com.krenog.myf.event.dto.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.krenog.myf.event.entities.Event;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.util.Objects;

public class CommonEventDto {
    @JsonProperty("id")
    @ApiModelProperty(notes = "Идентификатор события")
    private Long id;
    @JsonProperty(value = "name", required = true)
    @Size(min = 5, message = "Name should not be less length than 5")
    @ApiModelProperty(notes = "Название события", required = true)
    private String name;

    public CommonEventDto() {
    }

    public CommonEventDto(Event event) {
        this.id = event.getId();
        this.name = event.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommonEventDto)) return false;
        CommonEventDto that = (CommonEventDto) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
