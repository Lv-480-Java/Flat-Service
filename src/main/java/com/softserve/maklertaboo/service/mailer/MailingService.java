package com.softserve.maklertaboo.service.mailer;

import com.softserve.maklertaboo.dto.flat.FlatSearchParametersDto;
import com.softserve.maklertaboo.entity.Subscription;
import com.softserve.maklertaboo.entity.flat.Flat;
import com.softserve.maklertaboo.entity.flat.FlatSearchParameters;
import com.softserve.maklertaboo.entity.user.User;
import com.softserve.maklertaboo.mapping.flat.FlatSearchMapper;
import com.softserve.maklertaboo.repository.SubscriptionRepository;
import com.softserve.maklertaboo.repository.search.FlatFullTextSearch;
import com.softserve.maklertaboo.repository.user.UserRepository;
import com.softserve.maklertaboo.security.jwt.JWTTokenProvider;
import com.softserve.maklertaboo.service.telegram.TelegramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.softserve.maklertaboo.service.mailer.MailMessage.MAIL_BODY;
import static com.softserve.maklertaboo.service.mailer.MailMessage.MAIL_HEADER;

@Service
public class MailingService {

    private final EmailSenderService emailSenderService;
    private final FlatSearchMapper flatSearchMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final FlatFullTextSearch flatFullTextSearch;
    private final TelegramService telegramService;
    private final JWTTokenProvider jwtTokenProvider;

    @Autowired
    public MailingService(EmailSenderService emailSenderService,
                          FlatSearchMapper flatSearchMapper,
                          UserRepository userRepository,
                          SubscriptionRepository subscriptionRepository,
                          FlatFullTextSearch flatFullTextSearch,
                          TelegramService telegramService,
                          JWTTokenProvider jwtTokenProvider
    ) {
        this.emailSenderService = emailSenderService;
        this.flatSearchMapper = flatSearchMapper;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.flatFullTextSearch = flatFullTextSearch;
        this.telegramService = telegramService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public void sendFlatsByUserRequests() {
        List<User> usersWithSubscription = userRepository.findAll();
        for (User user : usersWithSubscription) {
            sendNotificationsForUser(user);
        }
    }

    private void sendNotificationsForUser(User user) {
        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());
        for (Subscription subscription : subscriptions) {
            Page<Flat> flats = flatFullTextSearch.search(subscription.getFlatSearchParameters(), pageable);
            if (!flats.isEmpty()) {
                sendNotifications(user, subscription);
                break;
            }
        }
    }

    private Set<User> getUsersWithSubscription() {
        return subscriptionRepository.
                findAllByActiveIsTrue()
                .stream()
                .map(Subscription::getUser)
                .collect(Collectors.toSet());
    }


    private void sendNotifications(User user, Subscription subscription) {
        emailSenderService.sendMessage(user.getEmail(), MAIL_HEADER, MAIL_BODY);
        if (subscription.isTelegram() && user.getTelegramUserData().getChatId() != null) {
            telegramService.sendNotification(user.getTelegramUserData().getChatId(), MAIL_HEADER);
        }
    }

    public void subscribe(FlatSearchParametersDto parameters) {
        FlatSearchParameters flatParameters = flatSearchMapper.convertToEntity(parameters);
        Subscription subscription = new Subscription();
        subscription.setFlatSearchParameters(flatParameters);
        subscription.setUser(jwtTokenProvider.getCurrentUser());
        subscription.setActive(true);
        subscriptionRepository.save(subscription);
    }

    public void unsubscribe() {
        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(jwtTokenProvider.getCurrentUser());
        subscriptions.forEach(x -> x.setActive(false));
        subscriptionRepository.saveAll(subscriptions);
    }

    public void turnOnTelegramNotifications() {
        User user = jwtTokenProvider.getCurrentUser();
        List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
        subscriptions.forEach(subscription -> subscription.setTelegram(true));
        subscriptionRepository.saveAll(subscriptions);
    }
}
