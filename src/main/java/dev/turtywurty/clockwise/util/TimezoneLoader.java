package dev.turtywurty.clockwise.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import dev.turtywurty.clockwise.Clockwise;
import net.minecraftforge.fml.ModList;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class TimezoneLoader {
    private static final Map<String, List<TimezoneDetails>> TIMEZONES = ImmutableMap.copyOf(TimezoneLoader.loadTimezones());

    public static Map<String, List<TimezoneDetails>> loadTimezones() {
        Path filePath = ModList.get().getModFileById(Clockwise.MOD_ID).getFile().findResource("country_timezones.json");
        try {
            String content = Files.readString(filePath, StandardCharsets.UTF_8);

            Type mapType = new TypeToken<Map<String, List<TimezoneDetails>>>(){}.getType();
            Gson gson = new GsonBuilder().create();
            return gson.fromJson(content, mapType);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to load timezones!", exception);
        }
    }

    public static void init() {}

    public static Map<String, List<TimezoneDetails>> getTimezones() {
        return TIMEZONES;
    }
}
