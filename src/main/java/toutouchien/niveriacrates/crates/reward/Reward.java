package toutouchien.niveriacrates.crates.reward;

import org.jspecify.annotations.NullMarked;

@NullMarked
public record Reward(RewardType type, Object data) {
}
