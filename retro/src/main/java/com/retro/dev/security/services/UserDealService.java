package com.retro.dev.security.services;

import com.retro.dev.models.Deal;
import com.retro.dev.models.User;
import com.retro.dev.repository.DealsRepository;
import com.retro.dev.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserDealService {
    @Autowired
    DealsRepository dealsDao;
    @Autowired
    UserRepository userRepository;

    public ResponseEntity<?> save(String username, Deal deal){
        Optional<User> user = userRepository.findByUsername(username);
        Deal newDeal = new Deal(user.get().getId(), deal.getName(), deal.getDescription(),deal.getCategoryid(), deal.getServiceid(),
                deal.getFromDate(), deal.getTodate(), deal.getStatus());
        Deal dealres = dealsDao.save(newDeal);
        if(dealres != null)
            return ResponseEntity.ok("deal saved");
        else
            return ResponseEntity.ok("failed to save the deal");
    }

    public ResponseEntity<?> update(String username, Deal did){
        Deal deal = dealsDao.findById(did.getId());
        deal.setName(did.getName());
        deal.setDescription(did.getDescription());
        deal.setFromDate(did.getFromDate());
        deal.setTodate(did.getTodate());
        deal.setCategoryid(did.getCategoryid());
        deal.setServiceid(did.getServiceid());
        Deal dealres = dealsDao.save(deal);
        if(dealres != null)
            return ResponseEntity.ok("deal updated");
        else
            return ResponseEntity.ok("failed to update the deal");
    }


    public List<Deal> listDeals(String username){
        Optional<User> user = userRepository.findByUsername(username);
        return dealsDao.findAllByUid(user.get().getId());

    }


}
