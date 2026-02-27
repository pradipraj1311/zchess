package com.zchess.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Piece {

    @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
            private Long id;

                @Enumerated(EnumType.STRING)
                    private PieceType type;

                        @Enumerated(EnumType.STRING)
                            private Color color;

                                private int position;

                                    private boolean moved;
                                    }