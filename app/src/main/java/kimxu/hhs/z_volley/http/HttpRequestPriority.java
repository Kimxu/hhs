package kimxu.hhs.z_volley.http;

/**
 * Priority values.  Requests will be processed from higher priorities to
 * lower priorities, in FIFO order.
 */
public enum HttpRequestPriority {
    LOW,
    NORMAL,
    HIGH,
    IMMEDIATE
}