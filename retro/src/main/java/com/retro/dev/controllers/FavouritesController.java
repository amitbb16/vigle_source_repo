package com.retro.dev.controllers;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.retro.dev.payload.request.FavouritesRequest;
import com.retro.dev.payload.response.FavouritesResponse;
import com.retro.dev.security.services.FavouriteServices;



@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/favourites")
public class FavouritesController {

    @Autowired
    FavouriteServices favouritesService;

    @PostMapping("/listAllFavouritesData")
    public FavouritesResponse listAllFavouritesData(Principal principal, @RequestBody FavouritesRequest listFavourites) {
        return favouritesService.listAllFavouritesData(principal, listFavourites);
    }
    
    @PostMapping("/listAllFavouritesBasedOnUser")
    public FavouritesResponse listAllFavouritesBasedOnUser(Principal principal, @RequestBody FavouritesRequest favouritesRequest) {
        return favouritesService.listAllUserFavourites(principal, favouritesRequest);
    }
    
    @PostMapping("/listAllFavouritesBasedOnService")
    public FavouritesResponse listAllFavouritesBasedOnService(Principal principal, @RequestBody FavouritesRequest favouritesRequest) {
        return favouritesService.listAllFavouritesBasedOnService(principal, favouritesRequest);
    }

    @PostMapping("/getFavouritesDetail")
    public FavouritesResponse getFavouritesDetail(Principal principal, @RequestBody FavouritesRequest favouritesRequest) {
        return favouritesService.getFavouritesDetail(principal, favouritesRequest);
    }

    @PostMapping("/markAsFavourite")
    public FavouritesResponse markAsFavourite(Principal principal, @RequestBody FavouritesRequest favouritesRequest) {
        return favouritesService.save(principal.getName(), favouritesRequest);
    }

    @PostMapping("/removeFromFavourite")
    public FavouritesResponse removeFromFavourite(Principal principal, @RequestBody FavouritesRequest favouritesRequest) {
        return favouritesService.removeFromFavourite(principal.getName(), favouritesRequest);
    }

    @PostMapping("/getServiceSpecificUserFavouriteDetail")
    public FavouritesResponse getServiceSpecificUserFavouriteDetail(Principal principal, @RequestBody FavouritesRequest favouritesRequest) {
        return favouritesService.getServiceSpecificUserFavouriteDetail(principal.getName(), favouritesRequest);
    }

    @PostMapping("/getOrderSpecificUserFavouritesDetail")
    public FavouritesResponse getOrderSpecificUserFavouritesDetail(Principal principal, @RequestBody FavouritesRequest favouritesRequest) {
        return favouritesService.getOrderSpecificUserFavouritesDetail(principal.getName(), favouritesRequest);
    }
}
