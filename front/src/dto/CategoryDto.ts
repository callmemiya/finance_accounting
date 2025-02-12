/**
 * DTO для сущности категории
 */
export interface CategoryDto {

    id: number;
    categoryName: string;
    categorySubstrings: string[];
    createdDatetime: Date;
    modifiedDatetime: Date;

}