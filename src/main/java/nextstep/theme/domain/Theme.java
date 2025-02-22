package nextstep.theme.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Theme {
    private Long id;
    private String name;
    private String desc;
    private int price;

    public Theme(String name, String desc, int price) {
        this(null, name, desc, price);
    }
}
