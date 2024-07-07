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
public class CustOrderDetailId {

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.productId);
        hash = 79 * hash + Objects.hashCode(this.custOrderId);
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
        final CustOrderDetailId other = (CustOrderDetailId) obj;
        if (!Objects.equals(this.productId, other.productId)) {
            return false;
        }
        return Objects.equals(this.custOrderId, other.custOrderId);
    }
    private Integer productId;
    private Integer custOrderId;
}
