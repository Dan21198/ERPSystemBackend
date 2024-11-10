package osu.mapper;

import org.mapstruct.Named;
import osu.enums.AcademicTitle;

public class CustomMappings {

    @Named("mapTitle")
    public static AcademicTitle mapTitle(String title) {
        return title != null ? AcademicTitle.valueOf(title) : null;
    }

    @Named("mapTitle")
    public static String mapTitle(AcademicTitle title) {
        return title != null ? title.toString() : null;
    }
}