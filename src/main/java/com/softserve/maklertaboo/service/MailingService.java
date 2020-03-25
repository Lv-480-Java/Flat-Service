package com.softserve.maklertaboo.service;

import com.softserve.maklertaboo.dto.flat.FlatSearchParametersDto;
import com.softserve.maklertaboo.entity.Subscription;
import com.softserve.maklertaboo.entity.flat.FlatSearchParameters;
import com.softserve.maklertaboo.entity.user.User;
import com.softserve.maklertaboo.mapping.flat.FlatSearchMapper;
import com.softserve.maklertaboo.repository.SubscriptionRepository;
import com.softserve.maklertaboo.repository.search.FlatFullTextSearch;
import com.softserve.maklertaboo.repository.user.UserRepository;
import com.softserve.maklertaboo.service.mailer.EmailSenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MailingService {

    private final EmailSenderService emailSenderService;
    private final FlatSearchMapper flatSearchMapper;
    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final FlatFullTextSearch flatFullTextSearch;

    @Autowired
    public MailingService(EmailSenderService emailSenderService,
                          FlatSearchMapper flatSearchMapper,
                          UserRepository userRepository,
                          SubscriptionRepository subscriptionRepository,
                          FlatFullTextSearch flatFullTextSearch) {
        this.emailSenderService = emailSenderService;
        this.flatSearchMapper = flatSearchMapper;
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.flatFullTextSearch = flatFullTextSearch;
    }

    public void checkFlatsByUserRequests() {
        Set<User> usersWithSubscription = subscriptionRepository.
                findAll()
                .stream()
                .map(Subscription::getUser)
                .collect(Collectors.toSet());

        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").descending());

        for(User user: usersWithSubscription){
                    List<Subscription> subscriptions = subscriptionRepository.findAllByUser(user);
                    for(Subscription subscription: subscriptions){
                        if(!flatFullTextSearch.search(subscription.getFlatSearchParameters(), pageable).isEmpty()){
                            emailSenderService.sendMessage(user.getEmail(),"New Flats upcoming Check our service","soem new flats");
                            break;
                        }
                    }
                }
    }

    public void subscribe(FlatSearchParametersDto parameters, String email) {
        FlatSearchParameters flatParameters = flatSearchMapper.convertToEntity(parameters);
        Subscription subscription = new Subscription();
        subscription.setFlatSearchParameters(flatParameters);
        subscription.setUser(userRepository.findUserByEmail(email));
        subscriptionRepository.save(subscription);
    }
}