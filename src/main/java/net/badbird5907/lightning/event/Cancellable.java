package net.badbird5907.lightning.event;

public interface Cancellable {
    boolean isCancelled();

    void setCancelled(boolean cancelled);

    class DefaultCancellable implements Cancellable {
        private boolean cancelled = false;

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public void setCancelled(boolean cancelled) {
            this.cancelled = cancelled;
        }
    }
}
