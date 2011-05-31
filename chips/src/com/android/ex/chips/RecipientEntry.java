/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ex.chips;

import android.net.Uri;

/**
 * Represents one entry inside recipient auto-complete list.
 */
public class RecipientEntry {

    public static final int ENTRY_TYPE_PERSON = 0;
    public static final int ENTRY_TYPE_SEP_NORMAL = 1;
    public static final int ENTRY_TYPE_SEP_WITHIN_GROUP = 2;

    public static final int ENTRY_TYPE_SIZE = 3;

    /** Separator entry dividing two persons or groups. */
    public static final RecipientEntry SEP_NORMAL =
            new RecipientEntry(ENTRY_TYPE_SEP_NORMAL);
    /** Separator entry dividing two entries inside a person or a group. */
    public static final RecipientEntry SEP_WITHIN_GROUP =
            new RecipientEntry(ENTRY_TYPE_SEP_WITHIN_GROUP);

    private final int mEntryType;

    /**
     * True when this entry is the first entry in a group, which should have a photo and display
     * name, while the second or later entries won't.
     */
    private boolean mIsFirstLevel;
    private final String mDisplayName;
    /** Destination for this contact entry. Would be an email address or a phone number. */
    private final String mDestination;
    private final int mContactId;
    private final boolean mIsDivider;

    private final Uri mPhotoThumbnailUri;

    /**
     * This can be updated after this object being constructed, when the photo is fetched
     * from remote directories.
     */
    private byte[] mPhotoBytes;

    private RecipientEntry(int entryType) {
        mEntryType = entryType;
        mDisplayName = null;
        mDestination = null;
        mContactId = -1;
        mPhotoThumbnailUri = null;
        mPhotoBytes = null;
        mIsDivider = true;
    }

    private RecipientEntry(
            int entryType, String displayName, String destination, int contactId) {
        mEntryType = entryType;
        mIsFirstLevel = false;
        mDisplayName = displayName;
        mDestination = destination;
        mContactId = contactId;
        mPhotoThumbnailUri = null;
        mPhotoBytes = null;
        mIsDivider = false;
    }

    private RecipientEntry(
            int entryType, String displayName, String destination, int contactId,
            Uri photoThumbnailUri) {
        mEntryType = entryType;
        mIsFirstLevel = true;
        mDisplayName = displayName;
        mDestination = destination;
        mContactId = contactId;
        mPhotoThumbnailUri = photoThumbnailUri;
        mPhotoBytes = null;
        mIsDivider = false;
    }

    /**
     * Construct a RecipientEntry from just an address that has been entered.
     * This address has not been resolved to a contact and therefore does not
     * have a contact id or photo.
     */
    public static RecipientEntry constructFakeEntry(String address) {
        return new RecipientEntry(
                ENTRY_TYPE_PERSON, address, address, -1, null);
    }

    public static RecipientEntry constructTopLevelEntry(
            String displayName, String destination, int contactId, Uri photoThumbnailUri) {
        return new RecipientEntry(
                ENTRY_TYPE_PERSON, displayName, destination, contactId, photoThumbnailUri);
    }

    public static RecipientEntry constructTopLevelEntry(
            String displayName, String destination, int contactId, String thumbnailUriAsString) {
        return new RecipientEntry(
                ENTRY_TYPE_PERSON, displayName, destination, contactId,
                (thumbnailUriAsString != null ? Uri.parse(thumbnailUriAsString) : null));
    }

    public static RecipientEntry constructSecondLevelEntry(
            String displayName, String destination, int contactId) {
        return new RecipientEntry(ENTRY_TYPE_PERSON, displayName, destination, contactId);
    }

    public int getEntryType() {
        return mEntryType;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public String getDestination() {
        return mDestination;
    }

    public int getContactId() {
        return mContactId;
    }

    public boolean isFirstLevel() {
        return mIsFirstLevel;
    }

    public Uri getPhotoThumbnailUri() {
        return mPhotoThumbnailUri;
    }

    /** This can be called outside main Looper thread. */
    public synchronized void setPhotoBytes(byte[] photoBytes) {
        mPhotoBytes = photoBytes;
    }

    /** This can be called outside main Looper thread. */
    public synchronized byte[] getPhotoBytes() {
        return mPhotoBytes;
    }

    public boolean isSeparator() {
        return mIsDivider;
    }
}