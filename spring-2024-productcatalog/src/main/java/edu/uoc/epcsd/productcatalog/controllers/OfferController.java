package edu.uoc.epcsd.productcatalog.controllers;

import edu.uoc.epcsd.productcatalog.controllers.dtos.CreateOfferRequest;
import edu.uoc.epcsd.productcatalog.controllers.dtos.EvaluateOfferRequest;
import edu.uoc.epcsd.productcatalog.entities.Offer;
import edu.uoc.epcsd.productcatalog.services.OfferService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@Log4j2
@RestController
@RequestMapping("/offers")
public class OfferController {

    @Autowired
    private OfferService offerService;

    @GetMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public List<Offer> getAllOffers() {
        log.trace("getAllOffers");

        return offerService.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> createOffer(@RequestBody @NotNull CreateOfferRequest request) {
        log.trace("createOffer - request {}", request);

        Offer offer = offerService.createOffer(
                request.getCategoryId(),
                request.getProductId(),
                request.getSerialNumber(),
                request.getUserId(),
                request.getDailyPrice()
        );

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{offerId}")
                .buildAndExpand(offer.getOfferId())
                .toUri();

        return ResponseEntity.created(uri).body(offer.getOfferId());
    }

    @PatchMapping("/{offerId}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Long> evaluateOffer(
            @PathVariable @NotNull Long offerId,
            @RequestBody @NotNull EvaluateOfferRequest request
    ) {
        log.trace("evaluateOffer - request {}", request);

        Offer offer = offerService.evaluateOffer(
                offerId,
                request.getDate(),
                request.getStatus()
        );

        return ResponseEntity.ok(offer.getOfferId());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Offer> findOffersByUser(@RequestParam @NotNull Long userId) {
        log.trace("findOffersByUser - user id {}", userId);

        return offerService.findOffersByUser(userId);
    }
}
