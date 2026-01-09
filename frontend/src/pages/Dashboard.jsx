import { useEffect, useState } from 'react';
import api from '../api/axios';
import { DollarSign, TrendingUp, TrendingDown, ArrowUpRight, ArrowDownRight } from 'lucide-react';

export default function Dashboard() {
    const [pnlData, setPnlData] = useState({
        totalIncomeAmount: 0,
        totalExpenseAmount: 0,
        totalPnl: 0,
    });
    const [loading, setLoading] = useState(true);

    const [recentTransactions, setRecentTransactions] = useState([]);

    useEffect(() => {
        fetchDashboardData();
    }, []);

    const fetchDashboardData = async () => {
        try {
            setLoading(true);
            const [pnlRes, incomeRes, expenseRes] = await Promise.all([
                api.get('/user/pnl'),
                api.get('/user/income/get', { params: { sortBy: 'incomeDate', order: 'desc' } }),
                api.get('/user/expense/get', { params: { sortBy: 'expenseDate', order: 'desc' } })
            ]);

            setPnlData(pnlRes.data);

            const incomes = incomeRes.data.map(i => ({
                id: i.incomeId,
                desc: i.incomeDesc,
                amount: i.incomeAmount,
                date: i.incomeDate,
                type: 'INCOME',
                category: i.incomeType
            }));

            const expenses = expenseRes.data.map(e => ({
                id: e.expenseId,
                desc: e.expenseDesc,
                amount: e.expenseAmount,
                date: e.expenseDate,
                type: 'EXPENSE',
                category: e.expenseType
            }));

            const combined = [...incomes, ...expenses].sort((a, b) => new Date(b.date) - new Date(a.date));
            setRecentTransactions(combined.slice(0, 5));
        } catch (error) {
            console.error('Error fetching dashboard data', error);
        } finally {
            setLoading(false);
        }
    };

    const formatCurrency = (amount) => {
        return new Intl.NumberFormat('en-IN', {
            style: 'currency',
            currency: 'INR',
        }).format(amount);
    };

    if (loading) {
        return <div className="flex h-full items-center justify-center text-gray-500">Loading Dashboard...</div>;
    }

    return (
        <div className="max-w-7xl mx-auto">
            <h2 className="text-2xl font-bold text-gray-800 mb-6">Dashboard Overview</h2>

            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
                <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <div className="flex items-center justify-between mb-4">
                        <h3 className="text-gray-500 text-sm font-medium">Total Balance</h3>
                        <div className="p-2 bg-blue-50 text-blue-600 rounded-lg">
                            <DollarSign className="w-5 h-5" />
                        </div>
                    </div>
                    <p className={`text-2xl font-bold ${pnlData.totalPnl >= 0 ? 'text-gray-900' : 'text-red-600'}`}>
                        {formatCurrency(pnlData.totalPnl)}
                    </p>
                    <p className="text-sm text-gray-500 mt-1">Net Earnings</p>
                </div>

                <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <div className="flex items-center justify-between mb-4">
                        <h3 className="text-gray-500 text-sm font-medium">Total Income</h3>
                        <div className="p-2 bg-green-50 text-green-600 rounded-lg">
                            <TrendingUp className="w-5 h-5" />
                        </div>
                    </div>
                    <p className="text-2xl font-bold text-gray-900">{formatCurrency(pnlData.totalIncomeAmount)}</p>
                    <div className="flex items-center gap-1 mt-1 text-sm text-green-600">
                        <ArrowUpRight className="w-4 h-4" />
                        <span>Inflow</span>
                    </div>
                </div>

                <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100">
                    <div className="flex items-center justify-between mb-4">
                        <h3 className="text-gray-500 text-sm font-medium">Total Expenses</h3>
                        <div className="p-2 bg-red-50 text-red-600 rounded-lg">
                            <TrendingDown className="w-5 h-5" />
                        </div>
                    </div>
                    <p className="text-2xl font-bold text-gray-900">{formatCurrency(pnlData.totalExpenseAmount)}</p>
                    <div className="flex items-center gap-1 mt-1 text-sm text-red-600">
                        <ArrowDownRight className="w-4 h-4" />
                        <span>Outflow</span>
                    </div>
                </div>
            </div>

            {/* Recent Transactions Section */}
            <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                <div className="px-6 py-4 border-b border-gray-100">
                    <h3 className="font-bold text-gray-800">Recent Transactions</h3>
                </div>
                {recentTransactions.length === 0 ? (
                    <div className="p-6 text-center text-gray-500 text-sm">No recent transactions found</div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="w-full text-left">
                            <thead className="bg-gray-50 border-b border-gray-100">
                                <tr>
                                    <th className="px-6 py-3 text-xs font-semibold text-gray-500 uppercase">Description</th>
                                    <th className="px-6 py-3 text-xs font-semibold text-gray-500 uppercase">Category</th>
                                    <th className="px-6 py-3 text-xs font-semibold text-gray-500 uppercase">Date</th>
                                    <th className="px-6 py-3 text-xs font-semibold text-gray-500 uppercase text-right">Amount</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                                {recentTransactions.map((item, index) => (
                                    <tr key={`${item.type}-${item.id}-${index}`} className="hover:bg-gray-50/50 transition-colors">
                                        <td className="px-6 py-3 text-sm text-gray-900 font-medium">{item.desc}</td>
                                        <td className="px-6 py-3">
                                            <span className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium ${item.type === 'INCOME' ? 'bg-green-100 text-green-800' : 'bg-red-100 text-red-800'
                                                }`}>
                                                {item.category}
                                            </span>
                                        </td>
                                        <td className="px-6 py-3 text-sm text-gray-500">{item.date}</td>
                                        <td className={`px-6 py-3 text-sm font-semibold text-right ${item.type === 'INCOME' ? 'text-green-600' : 'text-red-600'
                                            }`}>
                                            {item.type === 'INCOME' ? '+' : '-'} {formatCurrency(item.amount)}
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>
        </div>
    );
}
