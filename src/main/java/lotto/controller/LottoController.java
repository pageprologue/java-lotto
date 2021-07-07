package lotto.controller;

import lotto.domain.WinningStatistics;
import lotto.domain.*;
import lotto.view.InputView;
import lotto.view.OutputView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static lotto.domain.LottoTicketVendingMachine.TICKET_PRICE;

public class LottoController {
    public void run() {
        String inputPrice = InputView.getBuyingPrice();
        BuyingPrice buyingPrice = new BuyingPrice(inputPrice);
        int ticketAmount = buyingPrice.divide(TICKET_PRICE);
        OutputView.printTicketAmount(ticketAmount);

        LottoTicketVendingMachine lottoTicketVendingMachine = new LottoTicketVendingMachine();
        List<LottoTicket> lottoTickets = lottoTicketVendingMachine.issueTickets(buyingPrice);
        OutputView.printLottoTickets(lottoTickets);

        String inputWinningNumbers = InputView.getWinningNumber();
        List<Integer> splitWinningNumbers = InputView.split(inputWinningNumbers)
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int bonusNumber = Integer.parseInt(InputView.getBonusNumber());
        WinningNumbers winningNumbers = new WinningNumbers(splitWinningNumbers, bonusNumber);
        WinningStatistics winningStatistics = new WinningStatistics(winningNumbers);

        int totalPrize = 0;
        for (LottoTicket lottoTicket : lottoTickets) {
            int matchedWinningNumberCount = winningNumbers.matchedWinningNumberCount(lottoTicket);
            boolean matchedBonusNumber = winningNumbers.isMatchedBonusNumber(lottoTicket);
            int prize = LottoPrize.prize(matchedWinningNumberCount, matchedBonusNumber);
            totalPrize += prize;
        }
        Map<Integer, Long> groupByWinningNumber = winningStatistics.groupByWinningNumber(lottoTickets);

        float profitRate = winningStatistics.profitRate(ticketAmount, totalPrize);
        OutputView.printProfitRate(profitRate);
    }
}
