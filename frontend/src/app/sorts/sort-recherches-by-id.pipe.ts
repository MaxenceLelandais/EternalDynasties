import { Pipe, PipeTransform } from '@angular/core';

@Pipe({ name: 'sortBy' })
export class SortRecherchesById implements PipeTransform {
  transform(array: any[], path: string, order: number = 1): any[] {
    if (!array || !path) return array;
    return array.sort((a: any, b: any) => {
      const pathParts = path.split('.');
      const aValue = Number(pathParts.reduce((obj, key) => obj[key], a));
      const bValue = Number(pathParts.reduce((obj, key) => obj[key], b));


      if (aValue < bValue) return -1 * order;
      if (aValue > bValue) return 1 * order;
      return 0;
    });
  }
}