package top.theillusivec4.customfov;

import io.github.fablabsmc.fablabs.api.fiber.v1.builder.ConfigTreeBuilder;
import io.github.fablabsmc.fablabs.api.fiber.v1.exception.ValueDeserializationException;
import io.github.fablabsmc.fablabs.api.fiber.v1.schema.type.derived.ConfigTypes;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.FiberSerialization;
import io.github.fablabsmc.fablabs.api.fiber.v1.serialization.JanksonValueSerializer;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.ConfigTree;
import io.github.fablabsmc.fablabs.api.fiber.v1.tree.PropertyMirror;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class CustomFoVConfig {

  private static final PropertyMirror<Double> FLYING_MODIFIER = PropertyMirror
      .create(ConfigTypes.DOUBLE);
  private static final PropertyMirror<Double> FLYING_MAX = PropertyMirror
      .create(ConfigTypes.DOUBLE);
  private static final PropertyMirror<Double> FLYING_MIN = PropertyMirror
      .create(ConfigTypes.DOUBLE);

  private static final ConfigTree INSTANCE;

  static {
    ConfigTreeBuilder builder = ConfigTree.builder();

    builder.fork("flying");

    builder.beginValue("flyingModifier", ConfigTypes.DOUBLE, 1.0D)
        .withComment("The modifier to multiply by the original FoV modifier")
        .finishValue(FLYING_MODIFIER::mirror);

    builder.beginValue("flyingMax", ConfigTypes.DOUBLE, 10.0D)
        .withComment("The maximum FoV flying modifier value").finishValue(FLYING_MAX::mirror);

    builder.beginValue("flyingMin", ConfigTypes.DOUBLE, 10.0D)
        .withComment("The minimum FoV flying modifier value").finishValue(FLYING_MIN::mirror);

    builder.finishBranch();

    INSTANCE = builder;
  }

  public static void init() {
    JanksonValueSerializer serializer = new JanksonValueSerializer(false);
    Path path = Paths.get("config", CustomFoV.MODID + ".json5");

    try (OutputStream stream = new BufferedOutputStream(
        Files.newOutputStream(path, StandardOpenOption.WRITE, StandardOpenOption.CREATE_NEW))) {
      FiberSerialization.serialize(INSTANCE, stream, serializer);
    } catch (IOException e) {
      CustomFoV.LOGGER.error("Error serializing new config!");
      e.printStackTrace();
    }

    try (InputStream stream = new BufferedInputStream(
        Files.newInputStream(path, StandardOpenOption.READ, StandardOpenOption.CREATE))) {
      FiberSerialization.deserialize(INSTANCE, stream, serializer);
    } catch (IOException | ValueDeserializationException e) {
      CustomFoV.LOGGER.error("Error deserializing config!");
      e.printStackTrace();
    }
    bake();
  }

  public static double flyingModifier;
  public static double flyingMax;
  public static double flyingMin;

  private static void bake() {
    flyingModifier = FLYING_MODIFIER.getValue();
    flyingMax = FLYING_MAX.getValue();
    flyingMin = FLYING_MIN.getValue();
  }
}
