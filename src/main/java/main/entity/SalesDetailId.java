/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main.entity;

import jakarta.persistence.Embeddable;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author hp
 */
@Embeddable
@Getter
@Setter
@AllArgsConstructor
public class SalesDetailId {

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.salesId);
        hash = 59 * hash + Objects.hashCode(this.productId);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SalesDetailId other = (SalesDetailId) obj;
        if (!Objects.equals(this.salesId, other.salesId)) {
            return false;
        }
        return Objects.equals(this.productId, other.productId);
    }
    private Integer salesId;
    private Integer productId;
}
