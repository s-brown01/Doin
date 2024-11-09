package edu.carroll.doin_backend.web.service;

import edu.carroll.doin_backend.web.dto.EventDTO;
import edu.carroll.doin_backend.web.enums.FriendshipStatus;
import edu.carroll.doin_backend.web.enums.Visibility;
import edu.carroll.doin_backend.web.model.Event;
import edu.carroll.doin_backend.web.model.Image;
import edu.carroll.doin_backend.web.model.User;
import edu.carroll.doin_backend.web.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventRepository eventRepository;
    private final ImageService imageService;
    private final FriendService friendService;

    public EventServiceImpl(EventRepository eventRepository, ImageService imageService, FriendService friendService) {
        this.eventRepository = eventRepository;
        this.imageService = imageService;
        this.friendService = friendService;
    }

    @Override
    public Page<EventDTO> getPublicEvents(Pageable pageable) {
        if(pageable == null)
            return Page.empty();

        logger.info("Retrieving events with paging, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Page<Event> eventPage = eventRepository.findAllPublicEvents(pageable);
        Page<EventDTO> eventDTOPage = eventPage.map(EventDTO::new);

        logger.info("Successfully retrieved {} events", eventDTOPage.getTotalElements());
        return eventDTOPage;
    }

    @Override
    public Page<EventDTO> getUserEvents(Integer userId, Integer reqUserId, Pageable pageable) {
        if(pageable == null)
            return Page.empty();
        logger.info("Retrieving events with paging, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Set<Integer> friends = friendService.findFriendIdsByUserId(reqUserId, FriendshipStatus.CONFIRMED);
        Page<Event> eventPage;
        if(userId.equals(reqUserId) || friends.contains(userId)) {
            eventPage = eventRepository.findUserEvents(userId, Visibility.PRIVATE, pageable);
        }
        else{
            eventPage = eventRepository.findUserEvents(userId, Visibility.PUBLIC, pageable);
        }
        Page<EventDTO> eventDTOPage = eventPage.map(EventDTO::new);

        logger.info("Successfully retrieved {} events", eventDTOPage.getTotalElements());
        return eventDTOPage;
    }

    @Override
    public Page<EventDTO> getAll(Integer userId, Pageable pageable) {
        if(pageable == null)
            return Page.empty();
        logger.info("Retrieving events with paging, page: {}, size: {}", pageable.getPageNumber(), pageable.getPageSize());
        Set<Integer> friends = friendService.findFriendIdsByUserId(userId, FriendshipStatus.CONFIRMED);
        friends.add(userId);
        Page<Event> eventPage = eventRepository.findPublicOrFriendsEvents(friends, pageable);
        Page<EventDTO> eventDTOPage = eventPage.map(EventDTO::new);

        logger.info("Successfully retrieved {} events", eventDTOPage.getTotalElements());
        return eventDTOPage;
    }

    @Override
    public List<EventDTO> getUpcomingEvents(Integer userId) {
        List<Event> events = eventRepository.getUpcomingEvents(userId);
        return events.stream().map(EventDTO::new).toList();
    }


    @Override
    public EventDTO getById(Integer eventId, Integer userId) {
        logger.info("Retrieving event by ID: {}", eventId);
        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if(eventOpt.isEmpty()) {
            logger.warn("Event not found with ID: {}", eventId);
            return null;
        }
        Event event = eventOpt.get();
        logger.info("Successfully retrieved event with ID: {}", event);
        if (event.getVisibility() == Visibility.PUBLIC || event.getCreator().getId().equals(userId))
            return new EventDTO(event);
        Set<Integer> friends = friendService.findFriendIdsByUserId(userId, FriendshipStatus.CONFIRMED);
        if (friends.contains(event.getCreator().getId()))
            return new EventDTO(event);
        return null;
    }

    @Override
    public EventDTO add(EventDTO event) {
        logger.info("Adding new event: {}", event);
        if (event.getDescription() != null && event.getDescription().length() > 255) {
            event.setDescription(event.getDescription().substring(0, 255));
        }

        if (event.getVisibility() == null) {
            event.setVisibility(Visibility.PUBLIC);
        }
        Event newEvent = eventRepository.save(new Event(event));
        logger.info("Successfully added event with ID: {}", newEvent.getId());
        return new EventDTO(newEvent);
    }

    @Override
    public boolean joinUser(Integer eventId, Integer userId) {
        logger.info("User with ID {} joining event with ID {}", userId, eventId);
        Optional<Event> existingOpt = eventRepository.findById(eventId);
        if(existingOpt.isEmpty())
            return false;
        Event existing = existingOpt.get();
        if(existing.getTime().isBefore(LocalDateTime.now()) || existing.getCreator().getId().equals(userId) || existing.getJoiners().stream().anyMatch(a-> Objects.equals(a.getId(), userId))) {
            return false;
        }
        User user = new User();
        user.setId(userId);
        existing.addJoiner(user);
        eventRepository.save(existing);
        logger.info("User with ID {} successfully joined event with ID {}", userId, eventId);
        return true;
    }

    @Override
    public boolean addImage(Integer eventId, Integer userId, MultipartFile file) {
        logger.info("Adding image to event with ID {} by user with ID {}", eventId, userId);

        Optional<Event> eventOpt = eventRepository.findById(eventId);
        if (eventOpt.isEmpty()) {
            logger.warn("Event not found with ID: {}", eventId);
            return false;
        }

        Event event = eventOpt.get();
        if (event.getTime().isAfter(LocalDateTime.now()) || event.getImages().size() > 5 ||
                !(Objects.equals(event.getCreator().getId(), userId) ||
                        event.getJoiners().stream().anyMatch(j -> Objects.equals(j.getId(), userId)))) {
            logger.warn("User is not allowed to add image to this event");
            return false;
        }

        // Check if the uploaded file is an image
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            logger.warn("Invalid file type: {}. Only image files are allowed", contentType);
            return false;
        }

        // Check if the file size is within the 10 MB limit
        final long MAX_FILE_SIZE_MB = 10;
        final long MAX_FILE_SIZE_BYTES = MAX_FILE_SIZE_MB * 1024 * 1024; // 10 MB in bytes
        if (file.getSize() > MAX_FILE_SIZE_BYTES) {
            logger.warn("File size exceeds the limit. Uploaded file size: {} bytes, Max allowed: {} bytes", file.getSize(), MAX_FILE_SIZE_BYTES);
            return false;
        }

        try {
            Image img = imageService.save(file);
            event.addImage(img);
            eventRepository.save(event);
            logger.info("Successfully added image to event with ID {}", eventId);
            return true;
        } catch (Exception e) {
            logger.error("Failed to add image to event with ID {} due to exception: {}", eventId, e.getMessage());
            return false;
        }
    }
}