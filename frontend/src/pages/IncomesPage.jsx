import { useEffect, useState } from 'react';
import api from '../api/axios';
import { Plus, Trash2, Filter, ArrowUpDown } from 'lucide-react';

export default function IncomesPage() {
    const [incomes, setIncomes] = useState([]);
    const [loading, setLoading] = useState(true);
    const [isModalOpen, setIsModalOpen] = useState(false);

    // Filters
    const [error, setError] = useState('');

    // Filters
    const [filterType, setFilterType] = useState('');
    const [sortBy, setSortBy] = useState('date'); // default sort
    const [order, setOrder] = useState('desc');

    // Form
    const [formData, setFormData] = useState({
        incomeDesc: '',
        incomeType: 'SALARY',
        incomeAmount: '',
        incomeDate: '',
    });

    const incomeTypes = ['SALARY', 'INVESTMENT', 'TRADING'];

    useEffect(() => {
        fetchIncomes();
    }, [filterType, sortBy, order]);

    useEffect(() => {
        if (error) {
            const timer = setTimeout(() => setError(''), 3000);
            return () => clearTimeout(timer);
        }
    }, [error]);

    const fetchIncomes = async () => {
        try {
            const params = {
                order: order
            };
            if (filterType) params.incomeType = filterType;
            if (sortBy === 'date') params.sortBy = 'incomeDate';
            else if (sortBy === 'amount') params.sortBy = 'incomeAmount';
            else if (sortBy) params.sortBy = sortBy;

            const response = await api.get('/user/income/get', { params });
            setIncomes(response.data);
        } catch (error) {
            console.error('Error fetching incomes', error);
            setError(error.response?.data?.message || 'Failed to fetch incomes');
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = async (id) => {
        if (!window.confirm("Are you sure you want to delete this income?")) return;
        try {
            await api.delete(`/user/income/delete/${id}`);
            fetchIncomes(); // Refresh list
        } catch (error) {
            console.error('Error deleting income', error);
            setError(error.response?.data?.message || 'Failed to delete income');
        }
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            await api.post('/user/income/add', formData);
            setIsModalOpen(false);
            setFormData({
                incomeDesc: '',
                incomeType: 'SALARY',
                incomeAmount: '',
                incomeDate: '',
            });
            fetchIncomes();
        } catch (error) {
            console.error('Error adding income', error);
            setError(error.response?.data?.message || 'Failed to add income');
        }
    };

    const formatCurrency = (amount) => {
        return new Intl.NumberFormat('en-IN', {
            style: 'currency',
            currency: 'INR',
        }).format(amount);
    };

    return (
        <div className="max-w-6xl mx-auto">
            <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-8 gap-4">
                <div>
                    <h2 className="text-2xl font-bold text-gray-800">Incomes</h2>
                    <p className="text-gray-500 text-sm">Manage your income sources</p>
                </div>
                <button
                    onClick={() => setIsModalOpen(true)}
                    className="flex items-center gap-2 bg-blue-600 hover:bg-blue-700 text-white px-4 py-2.5 rounded-lg transition-colors shadow-sm"
                >
                    <Plus className="w-5 h-5" />
                    Add Income
                </button>
            </div>

            {error && (
                <div className="bg-red-50 text-red-600 p-4 rounded-xl mb-6 flex items-center justify-between shadow-sm border border-red-100 animate-fade-in">
                    <span>{error}</span>
                    <button onClick={() => setError('')} className="text-red-400 hover:text-red-700">&times;</button>
                </div>
            )}

            {/* Filters/Controls */}
            <div className="bg-white p-4 rounded-xl shadow-sm border border-gray-100 mb-6 flex flex-wrap gap-4 items-center">
                <div className="flex items-center gap-2">
                    <Filter className="w-4 h-4 text-gray-400" />
                    <select
                        value={filterType}
                        onChange={(e) => setFilterType(e.target.value)}
                        className="border-none bg-gray-50 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                    >
                        <option value="">All Types</option>
                        {incomeTypes.map(type => (
                            <option key={type} value={type}>{type}</option>
                        ))}
                    </select>
                </div>

                <div className="flex items-center gap-2">
                    <ArrowUpDown className="w-4 h-4 text-gray-400" />
                    <select
                        value={sortBy}
                        onChange={(e) => setSortBy(e.target.value)}
                        className="border-none bg-gray-50 rounded-lg px-3 py-2 text-sm focus:ring-2 focus:ring-blue-500 outline-none"
                    >
                        <option value="date">Sort by Date</option>
                        <option value="amount">Sort by Amount</option>
                    </select>
                </div>

                <div className="flex items-center gap-2 ml-auto">
                    <button
                        onClick={() => setOrder(order === 'asc' ? 'desc' : 'asc')}
                        className="text-sm font-medium text-blue-600 hover:bg-blue-50 px-3 py-1.5 rounded-lg transition-colors"
                    >
                        {order === 'asc' ? 'Ascending' : 'Descending'}
                    </button>
                </div>
            </div>

            {/* List */}
            <div className="bg-white rounded-xl shadow-sm border border-gray-100 overflow-hidden">
                {loading ? (
                    <div className="p-8 text-center text-gray-500">Loading incomes...</div>
                ) : incomes.length === 0 ? (
                    <div className="p-12 text-center">
                        <div className="text-gray-400 mb-2">No incomes found</div>
                        <p className="text-sm text-gray-500">Add a new income to get started</p>
                    </div>
                ) : (
                    <div className="overflow-x-auto">
                        <table className="w-full text-left">
                            <thead className="bg-gray-50 border-b border-gray-100">
                                <tr>
                                    <th className="px-6 py-4 text-xs font-semibold text-gray-500 uppercase">Description</th>
                                    <th className="px-6 py-4 text-xs font-semibold text-gray-500 uppercase">Type</th>
                                    <th className="px-6 py-4 text-xs font-semibold text-gray-500 uppercase">Date</th>
                                    <th className="px-6 py-4 text-xs font-semibold text-gray-500 uppercase text-right">Amount</th>
                                    <th className="px-6 py-4 text-xs font-semibold text-gray-500 uppercase text-center">Actions</th>
                                </tr>
                            </thead>
                            <tbody className="divide-y divide-gray-100">
                                {incomes.map((income) => (
                                    <tr key={income.incomeId} className="hover:bg-gray-50/50 transition-colors">
                                        <td className="px-6 py-4 text-sm text-gray-900 font-medium">{income.incomeDesc}</td>
                                        <td className="px-6 py-4">
                                            <span className="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-green-100 text-green-800">
                                                {income.incomeType}
                                            </span>
                                        </td>
                                        <td className="px-6 py-4 text-sm text-gray-500">{income.incomeDate}</td>
                                        <td className="px-6 py-4 text-sm text-green-600 font-semibold text-right">
                                            + {formatCurrency(income.incomeAmount)}
                                        </td>
                                        <td className="px-6 py-4 text-center">
                                            <button
                                                onClick={() => handleDelete(income.incomeId)}
                                                className="text-gray-400 hover:text-red-600 transition-colors p-1"
                                            >
                                                <Trash2 className="w-4 h-4" />
                                            </button>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}
            </div>

            {/* Add Modal */}
            {isModalOpen && (
                <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4 backdrop-blur-sm">
                    <div className="bg-white rounded-xl shadow-xl w-full max-w-md overflow-hidden">
                        <div className="px-6 py-4 border-b border-gray-100 flex justify-between items-center bg-gray-50/50">
                            <h3 className="font-bold text-gray-800">Add New Income</h3>
                            <button onClick={() => setIsModalOpen(false)} className="text-gray-400 hover:text-gray-600">
                                &times;
                            </button>
                        </div>
                        <form onSubmit={handleSubmit} className="p-6 space-y-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Description</label>
                                <input
                                    type="text"
                                    required
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.incomeDesc}
                                    onChange={(e) => setFormData({ ...formData, incomeDesc: e.target.value })}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Type</label>
                                <select
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.incomeType}
                                    onChange={(e) => setFormData({ ...formData, incomeType: e.target.value })}
                                >
                                    {incomeTypes.map(t => <option key={t} value={t}>{t}</option>)}
                                </select>
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Amount</label>
                                <input
                                    type="number"
                                    required
                                    min="0"
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.incomeAmount}
                                    onChange={(e) => setFormData({ ...formData, incomeAmount: e.target.value })}
                                />
                            </div>
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">Date</label>
                                <input
                                    type="date"
                                    required
                                    className="w-full px-3 py-2 border border-gray-300 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
                                    value={formData.incomeDate}
                                    onChange={(e) => setFormData({ ...formData, incomeDate: e.target.value })}
                                />
                            </div>
                            <div className="pt-4 flex gap-3">
                                <button
                                    type="button"
                                    onClick={() => setIsModalOpen(false)}
                                    className="flex-1 px-4 py-2 text-gray-700 bg-gray-100 hover:bg-gray-200 rounded-lg transition-colors"
                                >
                                    Cancel
                                </button>
                                <button
                                    type="submit"
                                    className="flex-1 px-4 py-2 text-white bg-blue-600 hover:bg-blue-700 rounded-lg transition-colors"
                                >
                                    Save Income
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            )}
        </div>
    );
}
