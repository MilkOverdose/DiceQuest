package com.dicequest.logic;

import com.dicequest.entities.Knight;

import java.io.*;
import java.util.Properties;

/**
 * Handles saving and loading game state to/from a properties file.
 */
public final class SaveManager {

    private static final String SAVE_FILE = "dicequest_save.properties";

    private static final String KEY_FLOOR          = "floor";
    private static final String KEY_HP             = "hp";
    private static final String KEY_MAX_HP         = "maxHp";
    private static final String KEY_BASE_DMG       = "baseDmg";
    private static final String KEY_DMG_MODIFIER   = "enemyDamageModifier";
    private static final String KEY_RESOLVE        = "resolveStacks";
    private static final String KEY_DMG_REDUCTION  = "damageReduction";
    private static final String KEY_FREE_TURNS     = "freeTurns";

    private SaveManager() {}

    /**
     * Saves the current game state to disk.
     *
     * @param state    current game state
     * @param modifier current enemy damage modifier
     * @throws IOException if the file cannot be written
     */
    public static void save(GameState state, double modifier) throws IOException {
        Knight knight = (Knight) state.getPlayer();
        Properties props = new Properties();

        props.setProperty(KEY_FLOOR,         String.valueOf(state.getFloor()));
        props.setProperty(KEY_HP,            String.valueOf(knight.getHp()));
        props.setProperty(KEY_MAX_HP,        String.valueOf(knight.getMaxHp()));
        props.setProperty(KEY_BASE_DMG,      String.valueOf(knight.getBaseDmg()));
        props.setProperty(KEY_DMG_MODIFIER,  String.valueOf(modifier));
        props.setProperty(KEY_RESOLVE,       String.valueOf(knight.getResolveStacks()));
        props.setProperty(KEY_DMG_REDUCTION, String.valueOf(knight.getDamageReduction()));
        props.setProperty(KEY_FREE_TURNS,    String.valueOf(knight.getFreeTurns()));

        try (FileOutputStream out = new FileOutputStream(SAVE_FILE)) {
            props.store(out, "Dice Quest Save File");
        }
    }

    /**
     * Loads a saved game from disk.
     *
     * @return a populated {@link SaveData} record
     * @throws IOException if the file cannot be read
     * @throws SaveCorruptException if the save data is invalid or missing fields
     */
    public static SaveData load() throws IOException, SaveCorruptException {
        File file = new File(SAVE_FILE);
        if (!file.exists()) throw new IOException("No save file found.");

        Properties props = new Properties();
        try (FileInputStream in = new FileInputStream(file)) {
            props.load(in);
        }

        try {
            int    floor        = Integer.parseInt(getRequired(props, KEY_FLOOR));
            int    hp           = Integer.parseInt(getRequired(props, KEY_HP));
            int    maxHp        = Integer.parseInt(getRequired(props, KEY_MAX_HP));
            int    baseDmg      = Integer.parseInt(getRequired(props, KEY_BASE_DMG));
            double modifier     = Double.parseDouble(getRequired(props, KEY_DMG_MODIFIER));
            int    resolve      = Integer.parseInt(getRequired(props, KEY_RESOLVE));
            double dmgReduction = Double.parseDouble(getRequired(props, KEY_DMG_REDUCTION));
            int    freeTurns    = Integer.parseInt(getRequired(props, KEY_FREE_TURNS));

            // Validate ranges
            if (floor < 1)         throw new SaveCorruptException("Invalid floor: " + floor);
            if (hp < 0)            throw new SaveCorruptException("Invalid HP: " + hp);
            if (maxHp < 1)         throw new SaveCorruptException("Invalid max HP: " + maxHp);
            if (hp > maxHp)        throw new SaveCorruptException("HP exceeds max HP.");
            if (baseDmg < 1)       throw new SaveCorruptException("Invalid base damage: " + baseDmg);
            if (modifier < 1.0)    throw new SaveCorruptException("Invalid damage modifier: " + modifier);
            if (resolve < 0 || resolve > 5) throw new SaveCorruptException("Invalid resolve stacks: " + resolve);
            if (dmgReduction < 0 || dmgReduction > 0.25) throw new SaveCorruptException("Invalid damage reduction: " + dmgReduction);
            if (freeTurns < 0)     throw new SaveCorruptException("Invalid free turns: " + freeTurns);

            return new SaveData(floor, hp, maxHp, baseDmg, modifier, resolve, dmgReduction, freeTurns);

        } catch (NumberFormatException e) {
            throw new SaveCorruptException("Save file contains non-numeric data: " + e.getMessage());
        }
    }

    /**
     * Deletes the save file if it exists.
     */
    public static void deleteSave() {
        new File(SAVE_FILE).delete();
    }

    /**
     * @return true if a save file exists on disk
     */
    public static boolean saveExists() {
        return new File(SAVE_FILE).exists();
    }

    private static String getRequired(Properties props, String key) throws SaveCorruptException {
        String value = props.getProperty(key);
        if (value == null) throw new SaveCorruptException("Missing key in save file: " + key);
        return value;
    }

    // ─────────────────────────────────────────────────────────────────
    // RECORDS & EXCEPTIONS
    // ─────────────────────────────────────────────────────────────────

    public record SaveData(
            int    floor,
            int    hp,
            int    maxHp,
            int    baseDmg,
            double enemyDamageModifier,
            int    resolveStacks,
            double damageReduction,
            int    freeTurns) {}

    public static class SaveCorruptException extends Exception {
        public SaveCorruptException(String message) {
            super(message);
        }
    }
}