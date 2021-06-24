package com.turtlemint.verticals.bidmint.bidmint.services.interfaces;

import com.turtlemint.verticals.bidmint.bidmint.dao.Seller;
import com.turtlemint.verticals.bidmint.bidmint.dto.SellerDTO;
import reactor.core.publisher.Mono;

public interface ISellerService {

    Mono<SellerDTO> createSeller(Seller seller);
}
