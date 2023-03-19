/*
 * CarbonChat
 *
 * Copyright (c) 2023 Josua Parks (Vicarious)
 *                    Contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package net.draycia.carbon.paper;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.draycia.carbon.api.users.ComponentPlayerResult;
import net.draycia.carbon.api.users.UserManager;
import net.draycia.carbon.common.users.CarbonPlayerCommon;
import net.draycia.carbon.common.users.SaveOnChange;
import net.draycia.carbon.common.users.UserManagerInternal;
import net.draycia.carbon.paper.users.CarbonPlayerPaper;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class PaperUserManager implements UserManagerInternal<CarbonPlayerPaper>, SaveOnChange {

    protected final UserManager<CarbonPlayerCommon> proxiedUserManager;

    public PaperUserManager(final UserManager<CarbonPlayerCommon> proxiedUserManager) {
        this.proxiedUserManager = proxiedUserManager;
    }

    @Override
    public CompletableFuture<ComponentPlayerResult<CarbonPlayerPaper>> carbonPlayer(final UUID uuid) {
        return this.proxiedUserManager.carbonPlayer(uuid).thenApply(PaperUserManager::wrapResult);
    }

    @Override
    public CompletableFuture<CarbonPlayerPaper> user(UUID uuid) {
        return this.proxiedUserManager.user(uuid).thenApply(CarbonPlayerPaper::new);
    }

    @Override
    public CompletableFuture<ComponentPlayerResult<CarbonPlayerPaper>> savePlayer(final CarbonPlayerPaper player) {
        return this.proxiedUserManager.savePlayer(player.carbonPlayerCommon()).thenApply(PaperUserManager::wrapResult);
    }

    @Override
    public CompletableFuture<ComponentPlayerResult<CarbonPlayerPaper>> saveAndInvalidatePlayer(final CarbonPlayerPaper player) {
        return this.proxiedUserManager.saveAndInvalidatePlayer(player.carbonPlayerCommon()).thenApply(PaperUserManager::wrapResult);
    }

    private static ComponentPlayerResult<CarbonPlayerPaper> wrapResult(final ComponentPlayerResult<CarbonPlayerCommon> result) {
        if (result.player() == null) {
            return new ComponentPlayerResult<>(null, result.reason());
        }

        return new ComponentPlayerResult<>(new CarbonPlayerPaper(result.player()), result.reason());
    }

    @Override
    public int saveDisplayName(final UUID id, final @Nullable Component component) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.saveDisplayName(id, component);
        }

        return -1;
    }

    @Override
    public int saveMuted(final UUID id, final boolean muted) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.saveMuted(id, muted);
        }

        return -1;
    }

    @Override
    public int saveDeafened(final UUID id, final boolean deafened) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.saveDeafened(id, deafened);
        }

        return -1;
    }

    @Override
    public int saveSpying(final UUID id, final boolean spying) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.saveSpying(id, spying);
        }

        return -1;
    }

    @Override
    public int saveSelectedChannel(final UUID id, final @Nullable Key selectedChannel) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.saveSelectedChannel(id, selectedChannel);
        }

        return -1;
    }

    @Override
    public int saveLastWhisperTarget(final UUID id, final @Nullable UUID lastWhisperTarget) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.saveLastWhisperTarget(id, lastWhisperTarget);
        }

        return -1;
    }

    @Override
    public int saveWhisperReplyTarget(final UUID id, final @Nullable UUID whisperReplyTarget) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.saveWhisperReplyTarget(id, whisperReplyTarget);
        }

        return -1;
    }

    @Override
    public int addIgnore(final UUID id, final UUID ignoredPlayer) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.addIgnore(id, ignoredPlayer);
        }

        return -1;
    }

    @Override
    public int removeIgnore(final UUID id, final UUID ignoredPlayer) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.removeIgnore(id, ignoredPlayer);
        }

        return -1;
    }

    @Override
    public int addLeftChannel(final UUID id, final Key channel) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.addLeftChannel(id, channel);
        }

        return -1;
    }

    @Override
    public int removeLeftChannel(final UUID id, final Key channel) {
        if (this.proxiedUserManager instanceof SaveOnChange saveOnChange) {
            return saveOnChange.removeLeftChannel(id, channel);
        }

        return -1;
    }

    @Override
    public void shutdown() {
        ((UserManagerInternal<?>) this.proxiedUserManager).shutdown();
    }

    @Override
    public CompletableFuture<Void> save(CarbonPlayerPaper player) {
        return ((UserManagerInternal<CarbonPlayerCommon>) this.proxiedUserManager).save(player.carbonPlayerCommon());
    }

    @Override
    public CompletableFuture<Void> loggedOut(final UUID uuid) {
        return ((UserManagerInternal<?>) this.proxiedUserManager).loggedOut(uuid);
    }
}
