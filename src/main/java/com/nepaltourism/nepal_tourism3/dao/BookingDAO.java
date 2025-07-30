package com.nepaltourism.nepal_tourism3.dao;

import com.nepaltourism.nepal_tourism3.model.Booking;
import com.nepaltourism.nepal_tourism3.util.AlertUtil;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class BookingDAO {
    private static final String FILE_PATH = "data/bookings.txt";
    private static final String DELIMITER = ";;";
    private static final ReentrantLock fileLock = new ReentrantLock();
    private static int lastId = 0;

    static {
        initializeLastId();
    }

    private static void initializeLastId() {
        fileLock.lock();
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                Files.createDirectories(Paths.get("data"));
                Files.createFile(Paths.get(FILE_PATH));
                return;
            }

            List<String> lines = Files.readAllLines(Paths.get(FILE_PATH));
            if (!lines.isEmpty()) {
                String[] parts = lines.get(lines.size() - 1).split(DELIMITER);
                if (parts.length > 0) {
                    lastId = Integer.parseInt(parts[0]);
                }
            }
        } catch (IOException | NumberFormatException e) {
            AlertUtil.showErrorAlert("Error initializing booking data: " + e.getMessage(), "Only administrators can access festival management");
        } finally {
            fileLock.unlock();
        }
    }

    public static int getBookingCount() {
        fileLock.lock();
        try {
            return (int) Files.lines(Paths.get(FILE_PATH)).count();
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Error counting bookings: " + e.getMessage(), "Only administrators can access festival management");
            return 0;
        } finally {
            fileLock.unlock();
        }
    }

    public static boolean saveBooking(Booking booking) {
        fileLock.lock();
        try {
            if (booking.getId() == 0) {
                booking.setId(++lastId);
            }

            String bookingData = bookingToString(booking);

            Files.write(Paths.get(FILE_PATH),
                    (bookingData + System.lineSeparator()).getBytes(),
                    StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND);

            return true;
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Error saving booking: " + e.getMessage(), "Only administrators can access festival management");
            return false;
        } finally {
            fileLock.unlock();
        }
    }

    public static boolean updateBooking(Booking booking) {
        fileLock.lock();
        try {
            Path tempFile = Paths.get(FILE_PATH + ".tmp");
            Path originalFile = Paths.get(FILE_PATH);

            List<String> updatedLines = Files.lines(originalFile)
                    .map(line -> {
                        String[] parts = line.split(DELIMITER);
                        if (parts.length > 0 && parts[0].equals(String.valueOf(booking.getId()))) {
                            return bookingToString(booking);
                        }
                        return line;
                    })
                    .collect(Collectors.toList());

            Files.write(tempFile, updatedLines);
            Files.deleteIfExists(originalFile);
            Files.move(tempFile, originalFile);

            return true;
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Error updating booking: " + e.getMessage(), "Only administrators can access festival management");
            return false;
        } finally {
            fileLock.unlock();
        }
    }

    public static boolean deleteBooking(int id) {
        fileLock.lock();
        try {
            Path tempFile = Paths.get(FILE_PATH + ".tmp");
            Path originalFile = Paths.get(FILE_PATH);

            List<String> remainingLines = Files.lines(originalFile)
                    .filter(line -> {
                        String[] parts = line.split(DELIMITER);
                        return parts.length == 0 || !parts[0].equals(String.valueOf(id));
                    })
                    .collect(Collectors.toList());

            Files.write(tempFile, remainingLines);
            Files.deleteIfExists(originalFile);
            Files.move(tempFile, originalFile);

            return true;
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Error deleting booking: " + e.getMessage(), "Only administrators can access festival management");
            return false;
        } finally {
            fileLock.unlock();
        }
    }

    public static List<Booking> getAllBookings() {
        fileLock.lock();
        try {
            if (!Files.exists(Paths.get(FILE_PATH))) {
                return new ArrayList<>();
            }

            return Files.lines(Paths.get(FILE_PATH))
                    .map(BookingDAO::stringToBooking)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Error loading bookings: " + e.getMessage(), "Only administrators can access festival management");
            return new ArrayList<>();
        } finally {
            fileLock.unlock();
        }
    }

    private static String bookingToString(Booking booking) {
        return String.join(DELIMITER,
                String.valueOf(booking.getId()),
                booking.getTourist(),
                booking.getAttraction(),
                booking.getGuide(),
                booking.getStartDate().toString(),
                booking.getEndDate().toString(),
                String.valueOf(booking.getGroupSize()),
                String.valueOf(booking.getTotalPrice()),
                booking.getStatus(),
                booking.getFestivalDiscount() != null ? booking.getFestivalDiscount() : "None"
        );
    }

    private static Booking stringToBooking(String line) {
        try {
            String[] parts = line.split(DELIMITER);
            if (parts.length >= 10) {
                return new Booking(
                        Integer.parseInt(parts[0]),
                        parts[1],
                        parts[2],
                        parts[3],
                        LocalDate.parse(parts[4]),
                        LocalDate.parse(parts[5]),
                        Integer.parseInt(parts[6]),
                        Double.parseDouble(parts[7]),
                        parts[8],
                        parts[9]
                );
            }
        } catch (Exception e) {
            AlertUtil.showErrorAlert("Error parsing booking data: " + line, "Only administrators can access festival management");
        }
        return null;
    }

    public static Booking getBookingById(int id) {
        fileLock.lock();
        try {
            return Files.lines(Paths.get(FILE_PATH))
                    .map(BookingDAO::stringToBooking)
                    .filter(booking -> booking != null && booking.getId() == id)
                    .findFirst()
                    .orElse(null);
        } catch (IOException e) {
            AlertUtil.showErrorAlert("Error finding booking: " + e.getMessage(), "Only administrators can access festival management");
            return null;
        } finally {
            fileLock.unlock();
        }
    }
}