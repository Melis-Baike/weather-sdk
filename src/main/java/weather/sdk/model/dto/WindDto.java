package weather.sdk.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import weather.sdk.model.entity.Wind;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class WindDto {
    private double speed;

    public static WindDto buildDto(Wind wind){
        return WindDto.builder()
                .speed(wind.getSpeed())
                .build();
    }
}
