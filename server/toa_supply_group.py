from dataclasses import dataclass

@dataclass
class ToaSupplyPlayer:
    # From request:
    player_name: str
    party_players: list[str]
    # Filled in by us:
    should_take: int

@dataclass
class ToaSupplyGroup:
    all_players_sorted: list[str]
    checked_in_players: dict[str, ToaSupplyPlayer]

    def __init__(self, from_player: ToaSupplyPlayer):
        self.all_players_sorted = sorted(from_player.party_players)
        self.check_in(from_player)

    def check_in(self, player: ToaSupplyPlayer):
        party = sorted(from_player.party_players)
        if party != self.all_players_sorted:
            raise Exception("WUT")
        self.checked_in_players[player.player_name] = player

    def get_group_id(self):
        return self.all_players_sorted[0]

    def is_complete(self):
        return len(self.all_players_sorted) == len(self.checked_in_players)
