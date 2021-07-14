package lotto.controller;

import lotto.domain.lotto.LottoTickets;
import lotto.domain.util.StringUtil;
import lotto.domain.vending.BuyingPrice;
import lotto.domain.vending.LottoTicketVendingMachine;
import lotto.domain.vending.TicketAmount;
import lotto.domain.winning.LottoRank;
import lotto.domain.winning.WinningNumbers;
import lotto.domain.winning.WinningStatistics;
import lotto.view.InputView;
import lotto.view.OutputView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LottoController {

    private static LottoTicketVendingMachine lottoTicketVendingMachine = new LottoTicketVendingMachine();

    public static void run() {
        String inputPrice = InputView.getBuyingPrice();
        BuyingPrice buyingPrice = new BuyingPrice(inputPrice);

        String inputManualCount = InputView.getManualCount();
        TicketAmount ticketAmount = new TicketAmount(buyingPrice, inputManualCount);

        List<String> inputManualNumbers = InputView.getManualNumbers(ticketAmount);
        LottoTickets lottoTickets = lottoTicketVendingMachine.issueTickets(ticketAmount, inputManualNumbers);

        OutputView.printTicketAmount(ticketAmount);
        String inputWinningNumbers = InputView.getWinningNumber();
        List<Integer> splitWinningNumbers = StringUtil.split(inputWinningNumbers)
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        int bonusNumber = Integer.parseInt(InputView.getBonusNumber());
        WinningNumbers winningNumbers = new WinningNumbers(splitWinningNumbers, bonusNumber);
        WinningStatistics winningStatistics = new WinningStatistics(winningNumbers);

        Map<LottoRank, Integer> ranks = winningStatistics.groupByHitCount(lottoTickets);
        OutputView.printWinningStatistics(ranks);

        float profitRate = winningStatistics.profitRate(ticketAmount, ranks);
        OutputView.printProfitRate(profitRate);
    }
}
