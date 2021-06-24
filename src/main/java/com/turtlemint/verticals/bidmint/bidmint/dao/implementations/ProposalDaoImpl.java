package com.turtlemint.verticals.bidmint.bidmint.dao.implementations;

import com.turtlemint.verticals.bidmint.bidmint.dao.Proposal;
import com.turtlemint.verticals.bidmint.bidmint.dao.interfaces.IProposalDao;
import reactor.core.publisher.Mono;

public class ProposalDaoImpl extends AbstractDAOImpl<Proposal> implements IProposalDao {

    @Override
    public Mono<Proposal> getProposalByFilters() {
        return null;
    }
}
