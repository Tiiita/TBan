package de.tiiita.util.mongodb.impl.player;

import de.tiiita.util.mongodb.DocumentAdapter;
import org.bson.Document;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class PlayerDocumentAdapter implements DocumentAdapter<PlayerDocument> {

    //TODO: Impl
    @Override
    public PlayerDocument fromBson(@NotNull Document document) {
        return null;
    }

    @Override
    public @NotNull Document toBson(@NotNull PlayerDocument document) {
        return null;
    }

    @Override
    public PlayerDocument getEmpty(UUID uuid) {
        return new PlayerDocument(uuid);
    }
}
