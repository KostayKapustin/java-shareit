package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.HashMap;
import java.util.Map;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingClient extends BaseClient {
    static final String API_PREFIX = "/bookings";
    Map<String, Object> parameters = new HashMap<>();

    public BookingClient(@Value("${shareit-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(
                                serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> addNewBooking(Long userId,
                                                BookingDto bookingDto) {
        return post("", userId, bookingDto);

    }

    public ResponseEntity<Object> approvalBookingById(Long userId, Long bookingId, boolean isApproved) {
        return patchBooking("/" + bookingId + "?approved=" + isApproved, userId);
    }

    public ResponseEntity<Object> getBookingById(Long userId, Long bookingId) {
        return get("/" + bookingId, userId);
    }

    public ResponseEntity<Object> getAllBookingsForBooker(Long userId, String state, Integer from, Integer size) {
        parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("" + "?state={state}&from={from}&size={size}", userId, parameters);
    }

    public ResponseEntity<Object> getAllBookingsForOwner(Long userId, String state, Integer from, Integer size) {

        parameters = Map.of(
                "state", state,
                "from", from,
                "size", size
        );
        return get("/owner" + "?state={state}&from={from}&size={size}", userId, parameters);
    }
}