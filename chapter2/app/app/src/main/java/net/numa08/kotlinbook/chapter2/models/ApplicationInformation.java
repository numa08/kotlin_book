package net.numa08.kotlinbook.chapter2.models;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public final class ApplicationInformation {


    @NonNull
    private final CharSequence label;
    @NonNull
    private final Drawable icon;
    @Nullable
    private final CharSequence description;
    private final int lightVibrantRGB;
    private final int vibrantRGB;
    private final int bodyTextColor;
    private final int titleTextColor;
    @NonNull
    private final String packageName;
    @NonNull
    private final ApplicationInfo applicationInfo;

    public ApplicationInformation(@NonNull CharSequence label,@NonNull Drawable icon,@Nullable CharSequence description, int lightVibrantRGB, int vibrantRGB, int bodyTextColor, int titleTextColor,@NonNull String packageName,@NonNull ApplicationInfo applicationInfo) {
        this.label = label;
        this.icon = icon;
        this.description = description;
        this.lightVibrantRGB = lightVibrantRGB;
        this.vibrantRGB = vibrantRGB;
        this.bodyTextColor = bodyTextColor;
        this.titleTextColor = titleTextColor;
        this.packageName = packageName;
        this.applicationInfo = applicationInfo;
    }

    @NonNull
    public CharSequence getLabel() {
        return label;
    }

    @NonNull
    public Drawable getIcon() {
        return icon;
    }

    @Nullable
    public CharSequence getDescription() {
        return description;
    }

    public int getLightVibrantRGB() {
        return lightVibrantRGB;
    }

    public int getVibrantRGB() {
        return vibrantRGB;
    }

    public int getBodyTextColor() {
        return bodyTextColor;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    @NonNull
    public String getPackageName() {
        return packageName;
    }

    @Nullable
    public ApplicationInfo getApplicationInfo() {
        return applicationInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ApplicationInformation that = (ApplicationInformation) o;

        if (lightVibrantRGB != that.lightVibrantRGB) return false;
        if (vibrantRGB != that.vibrantRGB) return false;
        if (bodyTextColor != that.bodyTextColor) return false;
        if (titleTextColor != that.titleTextColor) return false;
        if (!label.equals(that.label)) return false;
        if (!icon.equals(that.icon)) return false;
        if (description != null ? !description.equals(that.description) : that.description != null)
            return false;
        if (!packageName.equals(that.packageName)) return false;
        return applicationInfo != null ? applicationInfo.equals(that.applicationInfo) : that.applicationInfo == null;

    }

    @Override
    public int hashCode() {
        int result = label.hashCode();
        result = 31 * result + icon.hashCode();
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + lightVibrantRGB;
        result = 31 * result + vibrantRGB;
        result = 31 * result + bodyTextColor;
        result = 31 * result + titleTextColor;
        result = 31 * result + packageName.hashCode();
        result = 31 * result + (applicationInfo != null ? applicationInfo.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ApplicationInformation{" +
                "label=" + label +
                ", icon=" + icon +
                ", description=" + description +
                ", lightVibrantRGB=" + lightVibrantRGB +
                ", vibrantRGB=" + vibrantRGB +
                ", bodyTextColor=" + bodyTextColor +
                ", titleTextColor=" + titleTextColor +
                ", packageName='" + packageName + '\'' +
                ", applicationInfo=" + applicationInfo +
                '}';
    }
}
