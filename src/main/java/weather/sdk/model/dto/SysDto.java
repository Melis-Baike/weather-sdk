package weather.sdk.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import weather.sdk.model.entity.Sys;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class SysDto {
    private int sunrise;
    private int sunset;

    public static SysDto buildDto(Sys sys){
        return SysDto.builder()
                .sunrise(sys.getSunrise())
                .sunset(sys.getSunset())
                .build();
    }
}
