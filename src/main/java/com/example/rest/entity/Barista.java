package com.example.rest.entity;


import com.example.rest.entity.exception.NoValidIdException;
import com.example.rest.entity.exception.NoValidNameException;
import com.example.rest.entity.exception.NoValidTipSizeException;
import com.example.rest.entity.exception.NullParamException;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Barista entity. Contains full name, tip size and order list.
 * Full name is required. Others fields are initialized: tipSize = 0.1, orderList = empty list, id = -1;
 */
@Entity(name = "Barista")
@Table(name = "barista")
public class Barista {
    @Id
    @SequenceGenerator(
            name = "barista_sequence",
            sequenceName = "barista_sequence",
            allocationSize = 10
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "barista_sequence"
    )
    @Column(name = "id", nullable = false)
    private Long id = -1L;

    @Column(
            name = "full_name",
            nullable = false,
            updatable = false
    )
    private String fullName;

    @Column(name = "tip_size", nullable = false)
    private Double tipSize;

    @OneToMany(mappedBy = "barista", fetch = FetchType.LAZY)
    private List<Order> orderList = new ArrayList<>();

    /**
     * All field constructor.
     *
     * @param id        unequal entity identifier. Must be more not less than zero.
     * @param fullName  barista's full name. Can't be empty or null.
     * @param orderList list of orders prepared by the barista. Can't be null.
     * @param tipSize   tip that barista take per order, in percent. Can't be less than zero, NaN, infinite or null.
     * @throws NullParamException      thrown when one of param equals null.
     * @throws NoValidIdException      thrown when id less than zero.
     * @throws NoValidNameException    thrown when name is empty.
     * @throws NoValidTipSizeException thrown when tip size is NaN, Infinite or less than zero.
     */
    public Barista(Long id, String fullName, List<Order> orderList, Double tipSize) {
        this(fullName, tipSize);

        if (id == null || orderList == null)
            throw new NullParamException();
        if (id < 0)
            throw new NoValidIdException(id);


        this.id = id;
        this.orderList = new ArrayList<>(orderList);
    }

    /**
     * Constructor with full name and tip size params
     *
     * @param fullName barista's full name. Can't be empty or null.
     * @param tipSize  tip that barista take per order, in percent. Can't be less than zero, NaN, infinite or null.
     * @throws NullParamException      thrown when one of param equals null.
     * @throws NoValidNameException    thrown when name is empty.
     * @throws NoValidTipSizeException thrown when tip size is NaN, Infinite or less than zero.
     */
    public Barista(String fullName, Double tipSize) {
        if (fullName == null || tipSize == null)
            throw new NullParamException();
        if (fullName.isEmpty())
            throw new NoValidNameException();
        if (tipSize.isNaN() || tipSize.isInfinite() || tipSize < 0.0)
            throw new NoValidTipSizeException(tipSize);

        this.fullName = fullName;
        this.tipSize = tipSize;
    }


    /**
     * Empty constructor.
     */
    public Barista() {
    }

    /**
     * Get unequal identifier.
     *
     * @return unequal identifier.
     * @throws NoValidIdException thrown if id is not defined.
     */
    public Long getId() {
        if (id.equals(-1L))
            throw new NoValidIdException();
        return id;
    }

    /**
     * Set unequal identifier.
     *
     * @param id unequal identifier. Can't be less than zero.
     * @throws NullParamException thrown when one of param equals null.
     * @throws NoValidIdException thrown when id less than zero.
     */
    public void setId(Long id) {
        if (id == null)
            throw new NullParamException();
        if (id < 0)
            throw new NoValidIdException(id);

        this.id = id;
    }

    /**
     * Get full name of barista.
     *
     * @return full name.
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Set barista's full name.
     *
     * @param fullName barista's full name.
     * @throws NullParamException   thrown when one of param equals null.
     * @throws NoValidNameException thrown when name is empty.
     */
    public void setFullName(String fullName) {
        if (fullName == null)
            throw new NullParamException();
        if (fullName.isEmpty())
            throw new NoValidNameException();

        this.fullName = fullName;
    }

    /**
     * Get order's that prepared by this barista.
     *
     * @return order list.
     */
    public List<Order> getOrderList() {
        return orderList;
    }

    /**
     * Set order list. List of orders that prepared by this barista.
     *
     * @param orderList that prepared by this barista.
     * @throws NullParamException thrown when one of param equals null.
     */
    public void setOrderList(List<Order> orderList) {
        if (orderList == null)
            throw new NullParamException();

        this.orderList = new ArrayList<>(orderList);
    }

    /**
     * Get tip that barista take per order, in percent. Can't be less than zero, NaN, infinite or null.
     *
     * @return tip size in percent.
     */
    public Double getTipSize() {
        return tipSize;
    }

    /**
     * Set tip that barista take per order, in percent. Can't be less than zero, NaN, infinite or null.
     *
     * @param tipSize tip that barista take per order, in percent.
     * @throws NullParamException      thrown when one of param equals null.
     * @throws NoValidTipSizeException thrown when tip size is NaN, Infinite or less than zero.
     */
    public void setTipSize(Double tipSize) {
        if (tipSize == null)
            throw new NullParamException();
        if (tipSize.isNaN() || tipSize.isInfinite() || tipSize < 0.0)
            throw new NoValidTipSizeException(tipSize);

        this.tipSize = tipSize;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Barista barista)) return false;

        return Objects.equals(getId(), barista.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Barista{" +
                "id=" + id +
                ", name='" + fullName + '\'' +
                '}';
    }

}
