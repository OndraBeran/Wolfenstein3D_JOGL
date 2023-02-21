package model.renderdata;

public record RenderData(RayData[] rays, SpriteData[] enemies,
                         PlayerData player, GameStateData state) {
}
