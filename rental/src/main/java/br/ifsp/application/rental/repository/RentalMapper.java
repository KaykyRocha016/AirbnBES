package br.ifsp.application.rental.repository;

import br.ifsp.application.property.repository.PropertyMapper;
import br.ifsp.application.rental.create.ICreateRentalService;
import br.ifsp.application.user.repository.UserMapper;
import br.ifsp.domain.models.property.Property;
import br.ifsp.domain.models.rental.Rental;
import br.ifsp.domain.models.rental.RentalEntity;
import br.ifsp.domain.models.rental.RentalState;
import br.ifsp.domain.models.user.User;
import br.ifsp.domain.services.IUuidGeneratorService;
import br.ifsp.domain.shared.valueobjects.Price;

import java.math.BigDecimal;
import java.time.Clock;
import java.util.UUID;

public class RentalMapper {
    private final IUuidGeneratorService uuidGeneratorService;

    public RentalMapper(IUuidGeneratorService uuidGeneratorService) {
        this.uuidGeneratorService = uuidGeneratorService;
    }

    public Rental fromCreateRequestModel(
            ICreateRentalService.RequestModel requestModel,
            User user,
            Property property,
            BigDecimal value,
            Clock clock
    ) {
        return property.createRental(
                uuidGeneratorService.generate(),
                user,
                requestModel.startDate(),
                requestModel.endDate(),
                new Price(value),
                RentalState.PENDING,
                clock
        );
    }

    public static Rental fromCreateRequestModel(
            UUID rentalId,
            ICreateRentalService.RequestModel requestModel,
            User user,
            Property property,
            BigDecimal value,
            Clock clock
    ) {
        return property.createRental(
                rentalId,
                user,
                requestModel.startDate(),
                requestModel.endDate(),
                new Price(value),
                RentalState.PENDING,
                clock
        );
    }

    public static Rental toDomain(RentalEntity entity, Clock clock) {
        Property property = PropertyMapper.toDomain(entity.getPropertyEntity());

        return Rental.builder()
                .id(entity.getId())
                .user(UserMapper.toDomain(entity.getUserEntity()))
                .property(property)
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .value(entity.getValue())
                .state(entity.getState())
                .clock(clock)
                .build();
    }

    public static RentalEntity toEntity(Rental rental) {
        return RentalEntity.builder()
                .id(rental.getId())
                .userEntity(UserMapper.toEntity(rental.getUser()))
                .propertyEntity(PropertyMapper.toShallowEntity(rental.getProperty()))
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .value(rental.getValue())
                .state(rental.getState())
                .build();
    }
}

