import React, {useEffect} from 'react';
import {Modal} from 'react-bootstrap';
import {SelectBox, TextArea} from 'devextreme-react';
import {Button} from 'devextreme-react';
import {TransactionDto} from "../../dto/TransactionDto";
import {CategoryDto} from "../../dto/CategoryDto";
import {DateTimeFormat} from "../../utils/dateTimeFormat";

interface JoinTransactionsModalProps {
    show: boolean;
    onHide: () => void;
    onSave: (categoryId: number, description: string) => void;
    selectedTransactions: TransactionDto[];
    categories: CategoryDto[];
}

export const JoinTransactionsModal: React.FC<JoinTransactionsModalProps> = ({
    show,
    onHide,
    onSave,
    selectedTransactions,
    categories
}) => {
    const [selectedCategory, setSelectedCategory] = React.useState<number | null>(null);
    const [description, setDescription] = React.useState<string>('');

    // Сброс формы при открытии модального окна
    useEffect(() => {
        if (show) {
            setSelectedCategory(null);
            setDescription('');
        }
    }, [show]);

    const handleSave = () => {
        if (!selectedCategory) {
            alert('Пожалуйста, выберите категорию');
            return;
        }
        onSave(selectedCategory, description);
    };

    // Вычисляем общую сумму выбранных транзакций
    const totalAmount = selectedTransactions.reduce((sum, transaction) => 
        sum + transaction.operationAmount, 0
    );

    return (
        <Modal show={show} onHide={onHide} size="lg">
            <Modal.Header closeButton>
                <Modal.Title>Объединение транзакций</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="mb-4">
                    <h6>Выбранные транзакции:</h6>
                    <div className="table-responsive">
                        <table className="table table-sm">
                            <thead>
                                <tr>
                                    <th>Дата</th>
                                    <th>Карта</th>
                                    <th>Сумма</th>
                                    <th>Категория</th>
                                    <th>Описание</th>
                                </tr>
                            </thead>
                            <tbody>
                                {selectedTransactions.map(transaction => (
                                    <tr key={transaction.id}>
                                        <td>{new Date(transaction.transactionDate).toLocaleDateString()}</td>
                                        <td>{transaction.cardNumber}</td>
                                        <td>{transaction.operationAmount.toFixed(2)}</td>
                                        <td>{transaction.category}</td>
                                        <td>{transaction.description}</td>
                                    </tr>
                                ))}
                            </tbody>
                            <tfoot>
                                <tr>
                                    <td colSpan={2}><strong>Итого:</strong></td>
                                    <td><strong>{totalAmount.toFixed(2)}</strong></td>
                                    <td colSpan={2}></td>
                                </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>

                <div className="mb-3">
                    <label className="form-label">Категория объединенной транзакции</label>
                    <SelectBox
                        dataSource={categories}
                        displayExpr="categoryName"
                        valueExpr="id"
                        value={selectedCategory}
                        onValueChanged={e => setSelectedCategory(e.value)}
                        searchEnabled={true}
                        className="w-100"
                    />
                </div>

                <div className="mb-3">
                    <label className="form-label">Описание объединенной транзакции</label>
                    <TextArea
                        height={100}
                        value={description}
                        onValueChanged={e => setDescription(e.value)}
                        className="w-100"
                    />
                </div>
            </Modal.Body>
            <Modal.Footer>
                <Button
                    text="Отмена"
                    onClick={onHide}
                    stylingMode="contained"
                    type="normal"
                />
                <Button
                    text="Сохранить"
                    onClick={handleSave}
                    stylingMode="contained"
                    type="success"
                    disabled={!selectedCategory}
                />
            </Modal.Footer>
        </Modal>
    );
}; 