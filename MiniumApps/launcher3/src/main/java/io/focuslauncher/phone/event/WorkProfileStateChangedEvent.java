package io.focuslauncher.phone.event;

/**
 * Event fired when work profile state changes (available/unavailable/paused)
 */
public class WorkProfileStateChangedEvent {
    private boolean isAvailable;

    public WorkProfileStateChangedEvent(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
